package com.minsait.onesait.architecture.audit.aop;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsait.onesait.architecture.audit.AuditProperties;
import com.minsait.onesait.architecture.audit.aop.annotation.AuditField;
import com.minsait.onesait.architecture.audit.aop.annotation.AuditIgnore;
import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;
import com.minsait.onesait.architecture.audit.model.event.MethodDefinition;
import com.minsait.onesait.architecture.audit.processor.IProcessor;

import brave.Tracer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseAspect {

	/**
	 * Only for dependences only
	 */
	@SuppressWarnings("unused")
	@Autowired(required = false)
	private List<IProcessor<?>> autowiredBeans;

	@Autowired(required = false)
	@Qualifier("auditAsyncExecutor")
	private Executor asyncAudit;

	@Autowired
	protected Tracer tracer;

	@Autowired
	protected ApplicationContext applicationContext;

	@Getter
	@Autowired
	private HttpServletRequest request;

	@Autowired
	@Getter
	private HttpServletResponse response;

	@Getter
	@Autowired
	private AuditProperties properties;

	private Map<String, Pair<String, Class<? extends AuditEvent>>> beansMap;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		log.info("Initializing audit processors...");

		String[] names = applicationContext.getBeanNamesForType(ResolvableType.forClass(IProcessor.class));

		beansMap = new HashMap<>();

		for (String name : names) {
			log.debug("Initializing proccessor bean {}", name);
			IProcessor<AuditEvent> processor = applicationContext.getBean(name, IProcessor.class);
			Class<? extends AuditEvent> clazz = (Class<? extends AuditEvent>) GenericTypeResolver
					.resolveTypeArgument(AopUtils.getTargetClass(processor), IProcessor.class);
			if (clazz == null) {
				log.warn("The clazz of the bean '{}' can not be resolver", name);
				continue;
			}
			Component component = clazz.getAnnotation(Component.class);
			if (component.value().isEmpty()) {
				log.warn("Component value of class {} can not be empty", clazz);
				log.warn("The bean {} will be ignored", name);
			} else {

				Pair<String, Class<? extends AuditEvent>> pair = new ImmutablePair<>(component.value(), clazz);
				beansMap.put(name, pair);
			}

		}

	}

	/**
	 * Process an audit event from a joint point and notify all {@link IProcessor}
	 * of audit.
	 * 
	 * @param joinPoint     Joint point from aspect event
	 * @param postProcessor Interface to process AuditEvents before calling handle
	 *                      methods of IProcessors. Can be null.
	 * 
	 * @see IProcessor
	 */
	public void processAudit(JoinPoint joinPoint, @Nullable PostProcessAuditEvent postProcessor) {
		processAudit(joinPoint, null, null, postProcessor);
	}

	/**
	 * Process an audit event from a joint point and notify all
	 * {@link IProcessor} of audit.
	 * 
	 * @param joinPoint Joint point from aspect event
	 * @see IProcessor
	 */
	public void processAudit(JoinPoint joinPoint) {
		processAudit(joinPoint, null, null, null);
	}

	/**
	 * Process an audit event from a joint point and notify all
	 * {@link IProcessor} of audit. Can send the result return of the method
	 * audit.
	 * 
	 * @param joinPoint Joint point from aspect event
	 * @param result    The result return of the method audit. Can be null
	 */
	public void processAudit(JoinPoint joinPoint, @Nullable Object result) {
		processAudit(joinPoint, result, null, null);
	}

	/**
	 * Process an audit event from a joint point and notify all
	 * {@link IProcessor} of audit. Used with {@link AfterThrowing}
	 * 
	 * @param joinPoint Joint point from aspect event
	 * @param exception The exception thrown
	 */
	public void processAudit(JoinPoint joinPoint, Throwable exception) {
		processAudit(joinPoint, null, exception, null);
	}

	@SuppressWarnings("unchecked")
	private void processAudit(JoinPoint joinPoint, @Nullable Object result, @Nullable Throwable exception,
			@Nullable PostProcessAuditEvent postProcessor) {

		JsonNode output = null;

		if (result != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				String json = mapper.writeValueAsString(result);
				output = mapper.readTree(json);
			} catch (JsonProcessingException e) {
				throw new AuditException(e.getMessage(), e);
			}
		}

		for (Entry<String, Pair<String, Class<? extends AuditEvent>>> entry : beansMap.entrySet()) {
			AuditEvent event = this.prepareMessage(joinPoint, entry.getValue().getKey(), entry.getValue().getValue());
			event.setOutput(output);
			event.setTimeOutput(LocalDateTime.now());
			if (exception != null) {
				event.setMessage(exception.getMessage());
				if (exception.getClass().isAnnotationPresent(ResponseStatus.class)) {
					ResponseStatus resStatus = exception.getClass().getAnnotation(ResponseStatus.class);
					HttpStatus status = resStatus.value();
					if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
						status = resStatus.code();
					}

					event.setStatus(status.value());

				} else {
					event.setStatus(500);
				}
			}

			if (postProcessor != null) {
				postProcessor.postProcess(event);
			}

			IProcessor<AuditEvent> processor = applicationContext.getBean(entry.getKey(), IProcessor.class);
			if (asyncAudit != null) {
				asyncAudit.execute(() -> processor.handle(event));
			} else {
				processor.handle(event);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends AuditEvent> T prepareMessage(JoinPoint joinPoint, String beanName, Class<T> clazz) {
		MethodDefinition methodDefinition = this.getMethodDefinition(joinPoint);

		T beanClazz;
		if (clazz.equals(AuditEvent.class)) {
			beanClazz = (T) new AuditEvent();
		} else {
			beanClazz = applicationContext.getBean(beanName, clazz);
		}

		beanClazz.setTraceIdString(tracer.currentSpan().context().traceIdString());
		beanClazz.setSpanIdString(Long.toHexString(tracer.currentSpan().context().spanId()));
		beanClazz.setApp(properties.getApp());
		beanClazz.setServerName(request.getServerName());
		beanClazz.setHttpMethod(request.getMethod());
		beanClazz.setMethodDefinition(methodDefinition);

		log.trace("Setting headers");
		Map<String, Object> headers = new HashMap<>();

		Enumeration<String> itr = request.getHeaderNames();
		while (itr.hasMoreElements()) {
			String headerName = itr.nextElement();
			Enumeration<String> itrHeader = request.getHeaders(headerName);
			List<String> headerList = new ArrayList<>();
			while (itrHeader.hasMoreElements()) {
				headerList.add(itrHeader.nextElement());
			}
			if (headerList.size() == 1) {
				headers.put(headerName, headerList.get(0));
			} else {
				headers.put(headerName, headerList);
			}
		}

		beanClazz.setHeaders(headers);
		beanClazz.setHttpMethod(request.getMethod());
		beanClazz.setStatus(response.getStatus());
		beanClazz.setPath(request.getRequestURI());

		return beanClazz;
	}

	private MethodDefinition getMethodDefinition(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method methodReflection = signature.getMethod();
		Parameter[] parameters = methodReflection.getParameters();
		String[] parametersNames = signature.getParameterNames();

		Map<String, Object> params = new HashMap<>();
		Object[] signatureArgs = joinPoint.getArgs();

		if (signatureArgs.length == parameters.length && parameters.length == parametersNames.length) {
			for (int i = 0; i < signatureArgs.length; i++) {

				if (!parameters[i].isAnnotationPresent(AuditIgnore.class)) {
					AuditField annotationAuditField = parameters[i].getAnnotation(AuditField.class);
					Object result = signatureArgs[i];

					if (annotationAuditField != null && parameters[i].getType().equals(String.class)) {
						StringBuilder myString = new StringBuilder((String) result);
						int start = annotationAuditField.hideFirst();
						int end = annotationAuditField.hideLast();
						if (annotationAuditField.hideFirst() < myString.length()) {
							myString.replace(0, start, StringUtils.repeat("*", start));
						}
						if (annotationAuditField.hideLast() < myString.length()) {
							myString.replace(myString.length() - end, myString.length(),
									StringUtils.repeat("*", start));
						}
						result = myString.toString();

					}
					params.put(parametersNames[i], result);
				}
			}
		}

		MethodDefinition method = new MethodDefinition();
		method.setName(signature.toShortString());
		method.setParams(params);
		return method;
	}
}
