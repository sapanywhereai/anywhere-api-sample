package com.sap.integration.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.integration.base.BaseService;
import com.sap.integration.product.model.AnwProductDto;
import com.sap.integration.salesorder.model.IAnwDto;

/**
 * Class used for getting / posting of SAP Anywhere / ERP data.
 */
public class ProductService extends BaseService {

    public static final String PRODUCTS = "Products";

    private final Logger LOG = Logger.getLogger(ProductService.class);

    public ProductService(String service, Class<? extends IAnwDto> anwClass) {
        super(service, anwClass);
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }


    @Override
    public void postToErp(List<? extends IAnwDto> anwObjects) {
    	//post to erp
    }
}
