package com.minsait.onesait.architecture.audit.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.minsait.onesait.architecture.audit.exception.AuditException;
import com.minsait.onesait.architecture.audit.model.event.AuditEvent;
import com.minsait.onesait.architecture.audit.model.jpa.AuditModel;
import com.minsait.onesait.architecture.audit.repository.AuditRepository;
import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(value = "architecture.audit.destination.jpa.enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass({ CrudRepository.class, JsonType.class }) // Just to know if JPA and Hibernate types are compiled
@Slf4j
public class JpaProcessor implements IProcessor<AuditEvent> {

	@Autowired
	private AuditRepository repository;

	@Override
	public void init() {
		log.info("Initialized Jpa Processor");
	}

	@Override
	public void handle(AuditEvent event) throws AuditException {
		log.trace("Audit event {} - {}", event.getTraceIdString(), event.getSpanIdString());
		AuditModel model = new AuditModel(event);
		model = repository.save(model);
		log.trace("Saved jpa event uuid {}", model.getId());
	}

	@Override
	public void close() {
		log.info("Closed Jpa Processor");
	}

}
