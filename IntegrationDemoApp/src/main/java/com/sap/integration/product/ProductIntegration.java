package com.sap.integration.product;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.product.model.AnwProductDto;
import com.sap.integration.utils.DateUtil;

/**
 * Integration logic of products is defined in this class. Do not run both sync methods to avoid circle updating!
 */
public class ProductIntegration {

    private static final Logger LOG = Logger.getLogger(ProductIntegration.class);

    private static ProductService service;

    /**
     * Integration of products from APP to SAP Anywhere
     */
    public static void syncToSapAnywhere() throws Exception {
        DateTime start = DateTime.now();
        LOG.info("Start of integration Product APP to SAP Anywhere");

        service = new ProductService();
        
        /*
         * get data which needs to be post to SAP anywhere
         */
        AnwProductDto anwProducts = service.getData();
        
        service.postToAnywhere(anwProducts);

        LOG.info("End of integration Product APP to SAP Anywhere");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }

}
