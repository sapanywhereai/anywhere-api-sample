package com.sap.integration.customer;

import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.base.BaseService;
import com.sap.integration.salesorder.model.IAnwDto;

/**
 * Class used for for getting / posting of SAP Anywhere / ERP data.
 */
public class CustomerAnwClient extends BaseService {

    public static final String CUSTOMERS = "Customers";

    private final Logger LOG = Logger.getLogger(CustomerAnwClient.class);

    public CustomerAnwClient(String service, Class<? extends IAnwDto> anwClass) {
        super(service, anwClass);
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }


    @Override
    public void postToErp(List<? extends IAnwDto> anwObjects) {
    	//save to erp system
    }
}
