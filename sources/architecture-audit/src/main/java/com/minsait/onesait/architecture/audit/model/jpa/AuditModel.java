package com.minsait.onesait.architecture.audit.model.jpa;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.minsait.onesait.architecture.audit.model.event.AuditEvent;
import com.minsait.onesait.architecture.audit.model.event.MethodDefinition;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Getter;

@Entity
@Getter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class AuditModel {

	@Id
	private String id;

	private String traceId;

	private String spanId;

	private String serverName;

	@Column(columnDefinition = "text")
	private String message;

	private LocalDateTime timeInput;

	private String httpMethod;

	@Column(columnDefinition = "json")
	@Type(type = "jsonb")
	private Object output;

	@Column(columnDefinition = "json")
	@Type(type = "json")
	private Map<String, Object> headers;

	@Column(columnDefinition = "text")
	private String path;

	private int status;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private MethodDefinition methodDefinition;
	
	private String username;
	
	protected AuditModel() {
		// JPA Default Constructor
	}

	public AuditModel(AuditEvent event) {
		this.id = event.getId();
		this.traceId = event.getTraceIdString();
		this.spanId = event.getSpanIdString();
		this.serverName = event.getServerName();
		this.message = event.getMessage();
		this.timeInput = event.getTimeInput();
		this.httpMethod = event.getHttpMethod();
		this.output = event.getOutput();
		this.headers = event.getHeaders();
		this.path = event.getPath();
		this.status = event.getStatus();
		this.methodDefinition = event.getMethodDefinition();
		this.username = event.getUser();
	}

}
