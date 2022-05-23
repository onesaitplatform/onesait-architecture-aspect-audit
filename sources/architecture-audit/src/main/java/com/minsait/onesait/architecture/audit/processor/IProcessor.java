package com.minsait.onesait.architecture.audit.processor;

import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;

/**
 * Interface used to add new destinations to store audit information
 * 
 * @author Architecture Team
 *
 */
public interface IProcessor<T extends AuditEvent> {

	public abstract void init();

	/**
	 * Method to store audit information
	 * 
	 * @param auditEvent Audit Event contains all information
	 * @throws AuditException if there is any error
	 */
	public abstract void handle(T auditEvent) throws AuditException;

	public abstract void close();
}
