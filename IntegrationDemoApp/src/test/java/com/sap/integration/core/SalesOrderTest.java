package com.sap.integration.core;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.sap.integration.anywhere.oauth.AccessTokenLoader;
import com.sap.integration.core.integration.CustomerIntegration;
import com.sap.integration.core.integration.ProductIntegration;
import com.sap.integration.core.integration.SalesOrderIntegration;
import com.sap.integration.erp.dummy.entity.Customer;
import com.sap.integration.erp.dummy.entity.Item;
import com.sap.integration.erp.dummy.entity.SalesOrder;
import com.sap.integration.erp.dummy.entity.SalesOrderLine;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

public class SalesOrderTest {

    // only for developers - just for testing purposes
    public static void main(String[] args) {
        try {
            AccessTokenLoader.load();

            // PREREQUISITIES - BEGIN
            // ----------------------
            // 1) Insert customer into SAP Anywhere
            Customer customerForSalesOrder = createCustomersInErp("EdovCustCode", "EdovCustName");
            CustomerIntegration.syncToSapAnywhere();

            // 2) Insert item into SAP Anywhere
            Item itemForSalesOrder = createItemsInErp("myProduct", "myProductEdo");
            ProductIntegration.syncToSapAnywhere();

            // 3) Insert Sales Order into ERP
            createSalesOrderInErp(customerForSalesOrder, itemForSalesOrder);
            // ----------------------
            // PREREQUISITIES - END

            // 4) Move (CREATE) Sales Order from ERP into SAP Anywhere - Core functionality to be tested
            SalesOrderIntegration.syncToSapAnywhere();

            // 5) Update Sales Orders in ERP
            updateSalesOrdersInErp();
            
            // 6) UPDATE Sales Order in SAP Anywhere - Core functionality to be tested
            SalesOrderIntegration.syncToSapAnywhere();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Customer createCustomersInErp(String code, String name) {
        Customer customer = new Customer();
        customer.setCode(code);
        customer.setName(name);
        customer.setLastUpdateTime(DateTime.now());
        customer.persist();

        return customer;
    }

    private static Item createItemsInErp(String itemCode, String itemName) {
        Item item = new Item();
        item.setItemCode(itemCode);
        item.setItemName(itemName);
        item.persist();

        return item;
    }

    private static void createSalesOrderInErp(Customer cust, Item itm) {
        SalesOrder so = new SalesOrder();

        SalesOrderLine salesOrderLine = new SalesOrderLine();
        salesOrderLine.setQuantity(4);
        salesOrderLine.setInventoryUomQuantity(4);
        salesOrderLine.setUnitPrice(50.0);
        salesOrderLine.setLineTotal(200.0);
        salesOrderLine.setCalculationBase("BY_DEFAULT");
        salesOrderLine.setSalesOrder(so);
        salesOrderLine.setItem(itm);
        
        ArrayList<SalesOrderLine> salesOrderLines = new ArrayList<SalesOrderLine>();
        salesOrderLines.add(salesOrderLine);

        so.setBusinessPartner(cust);
        so.setLines(salesOrderLines);
        so.setChannelId(new Long(1));
        so.setPricingMethod("GROSS_PRICE");
        so.setDocTotal(200.0);
        so.setOrderTime(new DateTime(DateTime.now().getMillis()-1000000));
        so.setLastUpdateTime(DateTime.now());

        so.persist();
    }
    
    private static void updateSalesOrdersInErp() {

        List<SalesOrder> salesOrders = JpaLayer
                .find(SalesOrder.class,
                        "SELECT so FROM SalesOrder so");

    	for (SalesOrder so : salesOrders) {
    		if ((so.getLines()!=null) && (so.getLines().size() > 0)){
    			so.getLines().get(0).setQuantity(10); //Quantity value on line changed for update purposes
    			so.getLines().get(0).setInventoryUomQuantity(10);
    		}
	        so.setLastUpdateTime(DateTime.now());
	    	so.merge();
    	}
    }
}