package com.minsait.onesait.architecture.audit.processor;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsait.onesait.architecture.audit.AuditProperties;
import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;
import com.minsait.onesait.architecture.audit.model.event.KafkaEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(value = "architecture.audit.destination.kafka.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class KafkaProcessor implements IProcessor<AuditEvent> {

	@Autowired
	private AuditProperties auditProperties;

	@Autowired
	private KafkaTemplate<String, String> kafka;

	@Autowired
	private ObjectMapper mapper;

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handle(AuditEvent auditMessage) throws AuditException {
		KafkaEvent event = this.generateEvent(auditMessage);
		this.send(event);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public void send(KafkaEvent event) throws AuditException {
		String json;
		try {
			event.setTimestampCreation(new Timestamp(System.currentTimeMillis()));
			json = mapper.writeValueAsString(event);
			kafka.send(auditProperties.getDestination().getKafka().getTopic(), json);
		} catch (JsonProcessingException e) {
			log.error("{} Error sending data", this.getClass());
			throw new AuditException(e.getMessage(), e.getCause(), auditProperties.isTransactional());
		}
	}

	public KafkaEvent generateEvent(AuditEvent auditEvent) {
		KafkaEvent event = new KafkaEvent();
		event.setEventId(UUID.randomUUID().toString());
		event.setAuthor(auditEvent.getApp());
		event.setEventName("audit-message");
		event.setEventDataFormat("JSON");
		event.setPayLoad(auditEvent);
		event.setVersion(1);

		return event;
	}

}
