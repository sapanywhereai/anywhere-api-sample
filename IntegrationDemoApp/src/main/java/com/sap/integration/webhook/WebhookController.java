package com.sap.integration.webhook;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sap.integration.anywhere.AnwServiceCall;
import com.sap.integration.anywhere.AnwUrlUtil;
import com.sap.integration.anywhere.dto.AnwCustomerDto;
import com.sap.integration.anywhere.dto.AnwSalesOrderDto;
import com.sap.integration.utils.JsonUtil;
import com.sap.integration.utils.SHA256Algorithm;
import com.sap.integration.utils.UrlBuilder;
import com.sap.integration.utils.configuration.Property;
import com.sap.integration.utils.integrationdb.entity.IntegrationState;
import com.sap.integration.webhook.definition.BoChangeNotification;
import com.sap.integration.webhook.definition.LoginNotification;
import com.sap.integration.webhook.definition.WebhookEventType;

@RestController
@RequestMapping(value = "/Webhook")
public class WebhookController {
	private static Logger logger = Logger.getLogger(WebhookController.class);

	@RequestMapping(value = "/ListenCustomerCreated", method = RequestMethod.POST)
	public void ListenCustomerCreated(@RequestBody BoChangeNotification boPayload) {
		try {
			if (verifyWebhook(boPayload.getTimestamp(), boPayload.getHmac())) {
				logger.info(
						"-------------------------------------------------------------------------------------------------------");
				logger.info(String.format("Event Type: %s", boPayload.getPayload().getEvent_type()));
				logger.info(String.format("Resource Id: %s", boPayload.getPayload().getResource_id()));
				logger.info(String.format("Tenant Code: %s", boPayload.getPayload().getTenant_code()));
				List<AnwCustomerDto> customers = queryCustomer(boPayload.getPayload().getResource_id());
				if(!CollectionUtils.isEmpty(customers)){
					logger.info(String.format("Customer %s created", customers.get(0).getDisplayName()));
				}
			} else {
				throw new Exception("illegal webhook request!");
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	@RequestMapping(value = "/ListenSalesOrderCreated", method = RequestMethod.POST)
	public void ListenSalesOrderCreated(@RequestBody BoChangeNotification boPayload) {
		try {
			if (verifyWebhook(boPayload.getTimestamp(), boPayload.getHmac())) {
				logger.info(
						"-------------------------------------------------------------------------------------------------------");
				logger.info(String.format("Event Type: %s", boPayload.getPayload().getEvent_type()));
				logger.info(String.format("Resource Id: %s", boPayload.getPayload().getResource_id()));
				logger.info(String.format("Tenant Code: %s", boPayload.getPayload().getTenant_code()));
				AnwSalesOrderDto salesOrder = querySalesOrder(boPayload.getPayload().getResource_id());
				if(salesOrder != null){
					logger.info(String.format("SalesOrder has %d lines", salesOrder.getProductLines().size()));
				}
			} else {
				throw new Exception("illegal webhook request!");
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	// example "in"
	private List<AnwCustomerDto> queryCustomer(String resourceId) throws Exception{
		UrlBuilder urlBuilder = new UrlBuilder()
				.append(AnwUrlUtil.getOpenApiBaseUrl())
				.append("Customers")
				.parameter("filter", "id in [" + resourceId +"]");
		return (List<AnwCustomerDto>) JsonUtil.getObjects(AnwServiceCall.get(urlBuilder, null).getContent(),
        		AnwCustomerDto.class);
	}
	
	private AnwSalesOrderDto querySalesOrder(String resourceId) throws Exception{
		UrlBuilder urlBuilder = new UrlBuilder()
				.append(AnwUrlUtil.getOpenApiBaseUrl())
				.append("SalesOrders/").append(resourceId)
				.parameter("select", "customer,productLines")
				.parameter("expand", "productLines");
		return (AnwSalesOrderDto) JsonUtil.getObject(AnwServiceCall.get(urlBuilder, null).getContent(),
        		AnwSalesOrderDto.class);
	}
	
	// example "expand"
	
	@RequestMapping(value = "ListenUserLogin", method = RequestMethod.POST)
	public void ListenUserLogin(@RequestBody LoginNotification loginNotification) {
		try {
			if (verifyWebhook(loginNotification.getTimestamp(), loginNotification.getHmac())) {
				logger.info(
						"-------------------------------------------------------------------------------------------------------");
				logger.info(String.format("Event Type: %s", loginNotification.getPayload().getEventType()));
				logger.info(String.format("Email: %s", loginNotification.getPayload().getEmail()));
				logger.info(String.format("Last login: %s", loginNotification.getPayload().getLastLogin()));
				logger.info(String.format("Tenant Code: %s", loginNotification.getPayload().getTenantCode()));
			} else {
				throw new Exception("illegal webhook request!");
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private boolean verifyWebhook(String timestamp, String hmac) throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append("apiKey=").append(Property.getAppId()).append("&timestamp=").append(timestamp);

		String encodedCode = SHA256Algorithm.encode(Property.getAppSecret(), builder.toString());
		if (encodedCode.equals(hmac)) {
			return true;
		}
		return false;
	}
	
	@RequestMapping(value = "registerWebhook", method = RequestMethod.POST)
	public void registerWebhook(@RequestBody String url) throws Exception {
		doRegister(WebhookEventType.CUSTOMER_CREATE, url + "/Webhook/ListenCustomerCreated");
		doRegister(WebhookEventType.USER_LOGIN, url + "/Webhook/ListenUserLogin");
	}

	private void doRegister(String eventType, String url) throws Exception {
		String createCustomerCreatedURL = url;
		Property.setWebhookListenURL(eventType, createCustomerCreatedURL);
		Long id = AnwWebhookRegister.RegisterWebhook(eventType, url);
		Property.setWebhookEventId(eventType, id);
	}
}
