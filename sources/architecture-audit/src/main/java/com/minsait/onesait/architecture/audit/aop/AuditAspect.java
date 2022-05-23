package com.minsait.onesait.architecture.audit.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(value = "architecture.audit.enabled", havingValue = "true", matchIfMissing = true)
public class AuditAspect extends BaseAspect {

	@AfterReturning(pointcut = "@annotation(Audit)", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		result = getProperties().isOutput() ? result : null;
		processAudit(joinPoint, result);
	}

	@AfterThrowing(pointcut = "@annotation(Audit)", throwing = "exception")
	public void afterThrowing(JoinPoint joinPoint, Throwable exception) {
		processAudit(joinPoint, exception);
	}
}
