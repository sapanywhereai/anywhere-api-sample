package com.sap.integration.webhook.definition;

public class AnwWebhook {
	private Long id;
	private String eventType;
	private String callbackUrl;

	public AnwWebhook(String eventType, String callbackUrl) {
		super();
		this.eventType = eventType;
		this.callbackUrl = callbackUrl;
	}

	public AnwWebhook() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
}
