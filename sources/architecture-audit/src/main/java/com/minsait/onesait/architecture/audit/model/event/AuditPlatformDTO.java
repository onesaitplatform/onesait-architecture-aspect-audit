package com.minsait.onesait.architecture.audit.model.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditPlatformDTO {

	private String formatedTimeStamp;
	private String message;
	private String ontology;
	private String operationType;
	private String otherType;
	private String ResultOperation;
	private Integer timeStamp;
	private String type;

}
