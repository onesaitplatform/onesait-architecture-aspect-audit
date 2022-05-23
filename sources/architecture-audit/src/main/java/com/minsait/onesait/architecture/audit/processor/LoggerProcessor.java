package com.minsait.onesait.architecture.audit.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;

@Component
@ConditionalOnProperty(value = "architecture.audit.destination.logger.enabled", havingValue = "true", matchIfMissing = false)
public class LoggerProcessor implements IProcessor<AuditEvent> {

	@Autowired
	private FormatAuditEvent formatAuditMessage;

	private Logger logger;

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handle(AuditEvent auditEvent) throws AuditException {
		logger = LoggerFactory.getLogger(this.getClass().getName());
		String logText = formatAuditMessage.format(auditEvent);
		logger.info(logText);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}
}
