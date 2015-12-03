package com.sap.integration.core;

import java.util.List;

import com.sap.integration.anywhere.oauth.AccessTokenLoader;
import com.sap.integration.core.integration.ProductIntegration;
import com.sap.integration.erp.dummy.entity.Item;

public class ItemsTest {

    // only for developers - just for testing purposes
    public static void main(String[] args) {

    	try {
        	AccessTokenLoader.load();
        	
			ProductIntegration.syncFromSapAnywhere();
			
			/*List<Item> i = Item.getAll(Item.class);
			Item one = i.get(0);
			one.setItemName("UPDATED");*/
			
        	//createProductsInErp();
        	
        	//ProductIntegration.syncToSAPAnywhere();
			
			List<Item> results = Item.getAll(Item.class);
			
	        System.out.println("List of Items\n--------------");
	        for (Item p : results) {
	            System.out.println(p.toString());
	        }
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     } 
    
    private static void createProductsInErp(){
    	Item itm = new Item();
    	itm.setId(51L);
    	itm.setItemCode("TimestampTest");
    	itm.setItemName("UPDATE3");
    	itm.merge();
    }
}
