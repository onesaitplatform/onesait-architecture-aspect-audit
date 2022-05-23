package com.minsait.onesait.architecture.audit.model.event;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class KafkaEvent {
	private String eventId;

	private String eventName;

	private Timestamp timestampCreation;

	private String author;

	private int version;

	private String eventDataFormat;

	private AuditEvent payLoad;
}