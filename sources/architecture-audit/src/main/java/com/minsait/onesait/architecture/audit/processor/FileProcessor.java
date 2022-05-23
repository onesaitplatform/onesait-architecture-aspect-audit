package com.minsait.onesait.architecture.audit.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.minsait.onesait.architecture.audit.AuditProperties;
import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(value = "architecture.audit.destination.file.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class FileProcessor implements IProcessor<AuditEvent> {

	@Autowired
	private AuditProperties auditProperties;

	@Autowired
	private FormatAuditEvent formatAuditEvent;

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handle(AuditEvent auditMessage) throws AuditException {
		try {
			String line = formatAuditEvent.format(auditMessage);
			StringBuilder pathFile = new StringBuilder();
			pathFile.append(auditProperties.getDestination().getFile().getPath());
			pathFile.append(auditProperties.getDestination().getFile().getName());

			Files.write(Paths.get(pathFile.toString()), line.getBytes(), StandardOpenOption.APPEND,
					StandardOpenOption.CREATE);
		} catch (IOException e) {
			log.error("{} Trying to write in a file", e.getMessage());
			throw new AuditException(e.getMessage(), e.getCause());
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}
}
