package com.minsait.onesait.architecture.audit.aop;

import com.minsait.onesait.architecture.audit.model.event.AuditEvent;
import com.minsait.onesait.architecture.audit.processor.IProcessor;

/**
 * Interface for process an audit event
 * 
 * @author Architecture
 *
 * @see IProcessor
 */
public interface PostProcessAuditEvent {

	/**
	 * Method called by Audit before calling each {@link IProcessor} configured
	 * 
	 * @param <T>   AuditEvent or subclass to process
	 * @param event Audit event to process
	 */
	public <T extends AuditEvent> void postProcess(T event);
}
