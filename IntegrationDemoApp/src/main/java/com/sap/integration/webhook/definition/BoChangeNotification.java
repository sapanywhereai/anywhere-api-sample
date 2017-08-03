package com.sap.integration.webhook.definition;

public class BoChangeNotification extends Notification {
	private BoChangePayload payload;

	public BoChangePayload getPayload() {
		return payload;
	}

	public void setPayload(BoChangePayload payload) {
		this.payload = payload;
	}

}
