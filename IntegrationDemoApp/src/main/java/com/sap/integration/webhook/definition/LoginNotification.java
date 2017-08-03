package com.sap.integration.webhook.definition;

public class LoginNotification extends Notification{
	private LoginPayload payload;

	public LoginPayload getPayload() {
		return payload;
	}

	public void setPayload(LoginPayload payload) {
		this.payload = payload;
	}

}
