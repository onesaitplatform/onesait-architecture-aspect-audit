package com.minsait.onesait.architecture.audit.processor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsait.onesait.architecture.audit.AuditProperties;
import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;

@Service
public class FormatAuditEvent {

	@Autowired
	private AuditProperties auditProperties;

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public String format(AuditEvent auditEvent) {
		if (auditEvent == null) {
			throw new AuditException("Audit Event is null");
		}

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		String formatDateTime = now.format(formatter);

		String separator = auditProperties.getEvent().getSeparator();

		StringBuilder builder = new StringBuilder(formatDateTime).append(separator);
		builder.append(auditEvent.getId()).append(separator);
		builder.append(auditEvent.getTraceIdString()).append(separator);
		builder.append(auditEvent.getSpanIdString()).append(separator);
		builder.append(auditEvent.getApp()).append(separator);
		builder.append(auditEvent.getServerName()).append(separator);
		builder.append(auditEvent.getUser()).append(separator);
		builder.append(auditEvent.getHttpMethod()).append(separator);

		builder.append(auditEvent.getMethodDefinition().getName()).append(separator);
		Map<String, Object> parameters = auditEvent.getMethodDefinition().getParams();
		parameters.forEach((k, v) -> {
			builder.append(v).append(separator);
		});

		builder.append(auditEvent.getTimeInput().format(formatter)).append(separator);
		if (auditEvent.getTimeOutput() != null) {
			builder.append(auditEvent.getTimeOutput().format(formatter)).append(separator);
		}

		if (auditEvent.getMessage() != null) {
			builder.append(auditEvent.getMessage()).append(separator);
		}

		// TODO check how to show the output of operation
		if (auditEvent.getOutput() != null) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				Object json = mapper.readValue(auditEvent.getOutput().toString(), Object.class);
				String outputString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
				builder.append(outputString);
			} catch (IOException e) {
				throw new AuditException(e.getMessage(), e);
			}
		}

		builder.append("\n");
		return builder.toString();
	}
}
