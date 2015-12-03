package com.sap.integration.core.integration;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.sap.integration.anywhere.dto.AnwCustomerDto;
import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.core.service.BaseService;
import com.sap.integration.core.service.CustomerService;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.IErpDto;
import com.sap.integration.utils.DateUtil;

/**
 * Integration logic of customers is defined in this class. Do not run both sync methods to avoid circle updating!
 */
public class CustomerIntegration {

    private static final Logger LOG = Logger.getLogger(CustomerIntegration.class);

    private static BaseService service;

    /**
     * Integration of customers from SAP Anywhere to ERP.
     * 
     * @throws Exception possible exception during synchronization
     */
    public static void syncFromSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration SAP Anywhere to ERP");

        // get customer service
        service = new CustomerService(CustomerService.CUSTOMERS, AnwCustomerDto.class, ErpCustomerDto.class);

        // get SAP Anywhere customers to integrate
        List<IAnwDto> anwCustomers = service.getAnwObjects();

        // post SAP Anywhere customers to crate/update
        service.postToErp(anwCustomers);

        // log end
        LOG.info("End of integration SAP Anywhere to ERP");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }

    /**
     * Integration of customers from ERP to SAP Anywhere.
     * 
     * @throws Exception possible exception during synchronization
     */
    public static void syncToSapAnywhere() throws Exception {
        // log start
        DateTime start = DateTime.now();
        LOG.info("Start of integration ERP to SAP Anywhere");

        // get customer service
        service = new CustomerService(CustomerService.CUSTOMERS, AnwCustomerDto.class, ErpCustomerDto.class);

        // get ERP customers to integrate
        List<IErpDto> anwCustomers = service.getErpObjects();

        // post ERP customers to crate/update
        service.postToAnywhere(anwCustomers);

        // log end
        LOG.info("End of integration ERP to SAP Anywhere");
        LOG.info("Step duration time: " + DateUtil.getDurationTime(start, DateTime.now()));
    }
}
