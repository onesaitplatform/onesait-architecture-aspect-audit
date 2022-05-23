package com.minsait.onesait.architecture.audit.model.event;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class MethodDefinition implements Serializable {

	private static final long serialVersionUID = -730890752104043292L;

	private String name;

	private Map<String, Object> params;
}
