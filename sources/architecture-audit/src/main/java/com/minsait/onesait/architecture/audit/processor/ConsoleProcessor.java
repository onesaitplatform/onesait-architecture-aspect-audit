package com.minsait.onesait.architecture.audit.processor;

import java.io.PrintStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;

@Component
@ConditionalOnProperty(value = "architecture.audit.destination.console.enabled", havingValue = "true", matchIfMissing = false)
public class ConsoleProcessor implements IProcessor<AuditEvent> {

	@Autowired
	private FormatAuditEvent formatAuditMessage;

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handle(AuditEvent auditEvent) throws AuditException {
		if (auditEvent == null) {
			throw new AuditException("Audit Event should not be null");
		}
		String logText = formatAuditMessage.format(auditEvent);
		PrintStream stream = System.out;
		stream.println(logText);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}
}
