package com.minsait.onesait.architecture.audit.exception;

import org.springframework.beans.factory.annotation.Autowired;

import com.minsait.onesait.architecture.audit.AuditProperties;

import lombok.Getter;

public class AuditException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private String remedialAction;

	@Autowired
	private AuditProperties properties;

	public AuditException(String message, Throwable cause, String remedialAction) {
		super(message, cause);
		this.remedialAction = remedialAction;
	}

	public AuditException(String message, String remedialAction) {
		super(message);
		this.remedialAction = remedialAction;
	}

	public AuditException(String message, Throwable cause) {
		super(message, cause);
		if (properties.isTransactional()) {
			this.remedialAction = "It should be rollback";
		}
	}

	public AuditException(String remedialAction) {
		super();
		this.remedialAction = remedialAction;
	}
}
