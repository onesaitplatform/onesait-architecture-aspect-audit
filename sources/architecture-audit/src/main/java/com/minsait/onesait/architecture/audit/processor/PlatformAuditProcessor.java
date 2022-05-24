package com.minsait.onesait.architecture.audit.processor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsait.onesait.architecture.audit.AuditProperties;
import com.minsait.onesait.architecture.audit.AuditProperties.Login;
import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;
import com.minsait.onesait.architecture.audit.model.event.AuditPlatformDTO;
import com.minsait.onesait.architecture.audit.model.event.LoginOutput;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(value = "architecture.audit.destination.platform.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class PlatformAuditProcessor implements IProcessor<AuditEvent> {

	@Autowired
	private AuditProperties auditProperties;

	@Autowired
	private ObjectMapper mapper;

	private static final String HEADER_AUTHORIZATION_KEY = "Authorization";
	private static final String BEARER_CHECK = "Bearer ";

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void handle(AuditEvent auditEvent) throws AuditException {

		RestTemplate template = new RestTemplate();
		String accessToken = generateToken();

		if (accessToken != null && !"".equals(accessToken)) {
			List<AuditPlatformDTO> auditResult = new ArrayList<>();
			try {

				String jsonString = mapper.writeValueAsString(auditEvent);

				AuditPlatformDTO auditDto = AuditPlatformDTO.builder().message(jsonString)
						.formatedTimeStamp(LocalDateTime.now().toString()).operationType("APP-AUDIT")
						.ResultOperation("SUCCESS").timeStamp(0).type("USER").build();
				auditResult.add(auditDto);
			} catch (JsonProcessingException e1) {
				e1.printStackTrace();
			}

			String authorization = BEARER_CHECK + accessToken;
			HttpHeaders httpHeaders = createDefaultHttpAuthHeaders(authorization);
			HttpEntity<List<AuditPlatformDTO>> httpEntity = new HttpEntity<>(auditResult, httpHeaders);

			try {
				template.postForEntity(auditProperties.getDestination().getPlatform().getAuditUrl(), httpEntity,
						Void.class);
			} catch (RestClientException e) {
				log.error("Calling API platform for adding audit");
				throw (HttpClientErrorException) e;
			}
		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	private HttpHeaders createDefaultHttpAuthHeaders(String authorization) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.clear();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(Collections.singletonList(MediaType.ALL));
		httpHeaders.add(HEADER_AUTHORIZATION_KEY, authorization);
		return httpHeaders;
	}

	private String generateToken() {
		RestTemplate template = new RestTemplate();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.clear();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String base = Base64.getEncoder().encodeToString(("onesaitplatform:onesaitplatform").getBytes());
		httpHeaders.set("Authorization", "Basic " + base);

		Login login = auditProperties.getDestination().getPlatform().getLogin();

		MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
		mvm.add("grant_type", login.getGrant_type());
		mvm.add("scope", login.getScope());
		mvm.add("username", login.getUser());
		mvm.add("password", login.getPassword());
		mvm.add("vertical", login.getVertical());

		final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(mvm, httpHeaders);

		ResponseEntity<LoginOutput> auth = null;

		try {
			log.debug("Start login to the platform: " + login.getUrl());
			auth = template.postForEntity(login.getUrl(), entity, LoginOutput.class);
			log.debug("login response: " + auth.toString());

			return auth.getBody().getAccessToken();
		} catch (RestClientException e) {
			log.debug("Error using RestTemplate", e);
			throw (HttpClientErrorException) e;
		}
	}

}
