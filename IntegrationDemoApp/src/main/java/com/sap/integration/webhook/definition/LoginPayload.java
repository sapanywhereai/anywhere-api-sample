package com.sap.integration.webhook.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginPayload {
	private String eventType;
	private String email;
	private String lastLogin;
	private String tenantCode;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonSetter("tenant_code")
	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	public String getTenantCode() {
		return tenantCode;
	}

	public String getEventType() {
		return eventType;
	}

	@JsonSetter("event_type")
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	@JsonSetter("last_login")
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
}
