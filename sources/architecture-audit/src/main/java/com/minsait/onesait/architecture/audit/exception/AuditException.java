package com.minsait.onesait.architecture.audit.exception;

import lombok.Getter;

public class AuditException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Getter
	private String remedialAction;

	public AuditException(String message, Throwable cause, String remedialAction) {
		super(message, cause);
		this.remedialAction = remedialAction;
	}

	public AuditException(String message, String remedialAction) {
		super(message);
		this.remedialAction = remedialAction;
	}

	public AuditException(String message, Throwable cause, boolean isTransactional) {
		super(message, cause);
		if (isTransactional) {
			this.remedialAction = "It should be rollback";
		}
	}

	public AuditException(String remedialAction) {
		super();
		this.remedialAction = remedialAction;
	}
}
