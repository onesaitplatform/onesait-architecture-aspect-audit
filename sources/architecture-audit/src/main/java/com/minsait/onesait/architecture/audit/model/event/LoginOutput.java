package com.minsait.onesait.architecture.audit.model.event;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginOutput {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private long expiresIn;

	@JsonProperty("scope")
	private String scope;

	@JsonProperty("principal")
	private String principal;

	@JsonProperty("clientId")
	private String clientId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("vertical")
	private String vertical;

	@JsonProperty("grantType")
	private String grantType;

	@JsonProperty("parameters")
	private Object parameters;

	@JsonProperty("verticals")
	private List<String> verticals;

	@JsonProperty("tenant")
	private String tenant;

	@JsonProperty("authorities")
	private List<String> authorities;

	@JsonProperty("jti")
	private String jti;

}
