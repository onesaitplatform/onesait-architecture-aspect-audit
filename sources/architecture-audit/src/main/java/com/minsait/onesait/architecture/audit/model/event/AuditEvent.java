package com.minsait.onesait.architecture.audit.model.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
@Scope("prototype")
@Component("AuditEvent")
public class AuditEvent implements Serializable {

	private static final long serialVersionUID = -7441580665441716297L;

	// Autowired requests
	@Autowired
	@JsonIgnore
	private transient HttpServletRequest request;

	@Autowired
	@JsonIgnore
	private transient HttpServletResponse response;

	private String id;

	private String traceIdString;

	private String spanIdString;

	private String app;

	private String serverName;

	private String user;

	private MethodDefinition methodDefinition;

	private String message;

	private LocalDateTime timeInput;

	private LocalDateTime timeOutput;

	private String httpMethod;

	private transient JsonNode output;

	private transient Map<String, Object> headers;

	private String path;

	private int status;

	public AuditEvent() {
		this.id = UUID.randomUUID().toString();
		this.timeInput = LocalDateTime.now();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		this.user = authentication != null ? authentication.getName() : "ANONYMOUS";
	}
}
