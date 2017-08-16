package com.sap.integration.product;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.base.BaseService;
import com.sap.integration.product.model.AnwProductDto;
import com.sap.integration.salesorder.model.IAnwDto;
import com.sap.integration.utils.DateUtil;

/**
 * Integration logic of products is defined in this class. Do not run both sync methods to avoid circle updating!
 */
public class ProductIntegration {

    private static final Logger LOG = Logger.getLogger(ProductIntegration.class);

    private static BaseService service;

    /**
     * Integration of products from SAP Anywhere to ERP
     * 
     * @throws Exception possible exception during the processing
     */
    public static void syncFromSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration SAP Anywhere to ERP");

        // get product service
        service = new ProductService(ProductService.PRODUCTS, AnwProductDto.class);

        // get SAP Anywhere product to integrate
        List<IAnwDto> anwProducts = service.getAnwObjects();

        // post SAP Anywhere products to crate/update
        service.postToErp(anwProducts);

        // log end
        LOG.info("End of integration SAP Anywhere to ERP");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }

    /**
     * Integration of products from ERP to SAP Anywhere
     * 
     * @throws Exception possible exception during the processing
     */
    public static void syncToSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration ERP to SAP Anywhere");

        // get product service
        service = new ProductService(ProductService.PRODUCTS, AnwProductDto.class);

        // get ERP product to integrate
//        List<IErpDto> anwProducts = service.getErpObjects();

        // post ERP product to crate/update
//        service.postToAnywhere(anwProducts);

        // log end
        LOG.info("End of integration ERP to SAP Anywhere");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }

}
