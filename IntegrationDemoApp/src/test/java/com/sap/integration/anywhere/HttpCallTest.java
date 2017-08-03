package com.sap.integration.anywhere;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.apache.log4j.Logger;

import com.sap.integration.core.transformation.ProductTransformation;
import com.sap.integration.erp.dummy.conversion.ProductConversion;
import com.sap.integration.erp.dummy.entity.Item;
import com.sap.integration.utils.HttpsCall;
import com.sap.integration.utils.SimpleResponse;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

public class HttpCallTest {

	@Tested
	private HttpsCall httpsCall;

	private static final Logger LOG = Logger.getLogger(HttpsCall.class);

	// public static void main(String[] args) throws Exception {
	// HttpCallTest htc = new HttpCallTest();
	// // htc.tstGetItem();
	// // htc.tstPostItem();
	// httpsCall = new HttpsCall();
	// htc.tstPutItem();
	// }

	@Test
	public void tstGetItem() throws Exception {
		String link = "https://cnpvgvb1ep013.pvgl.sap.corp:49194/api-gateway/v1/Products?limit=100&offset=0";
		System.out.println(link);

		SimpleResponse response = httpsCall.get(new URI(link), new HashMap<String, String>() {
			{
				put("Access-Token", "b438c076-9309-496f-b834-dc3425adeee0");
			}
		});
		LOG.info(response.getContent());
		LOG.info(response.getStatusCode());
	}

	private void testPostItem() throws Exception {
		String link = "https://cnpvgvb1ep013.pvgl.sap.corp:49194/api-gateway/v1/Products";
		System.out.println(link);

		String json = "{\"code\" : \"bubu\", \"inventoryEnabled\" : true, \"isBackOrderAllowed\" : false, \"isDropShipAllowed\" : false, \"name\" : \"fromstring\", \"skuComponentEnabled\" : false, \"status\" : \"Active\", \"type\" : \"SingleInventory\" }";
		System.out.println(json);

		Item newitm = new Item();
		newitm.setItemCode("itmcode1");
		newitm.setItemName("itmname11");

		// SimpleResponse response = HttpsCall.post(new URI(link),json);
		SimpleResponse response = httpsCall.post(new URI(link),
				ProductTransformation.run(ProductConversion.run(newitm)), new HashMap<String, String>() {
					{
						put("Access-Token", "b438c076-9309-496f-b834-dc3425adeee0");
					}
				});
		LOG.info(response.getContent());
		LOG.info(response.getStatusCode());
	}

	@Test
	public void tstPutItem() throws Exception {
		String link = "https://cnpvgvb1ep013.pvgl.sap.corp:49194/api-gateway/v1/Products/28";
		System.out.println(link);

		String json = "{\"code\" : \"itmcode1\", \"name\" : \"UPDATE\"}";
		System.out.println(json);

		SimpleResponse response = httpsCall.patch(new URI(link), json, new HashMap<String, String>() {
			{
				put("Access-Token", "b438c076-9309-496f-b834-dc3425adeee0");
			}
		});
		LOG.info(response.getContent());
		LOG.info(response.getStatusCode());
	}

}