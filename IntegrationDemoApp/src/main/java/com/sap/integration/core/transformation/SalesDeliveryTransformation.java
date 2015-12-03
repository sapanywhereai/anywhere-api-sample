package com.sap.integration.core.transformation;

import java.util.ArrayList;

import com.sap.integration.anywhere.dto.AnwMarketingDocumentCustomerDto;
import com.sap.integration.anywhere.dto.AnwSalesDeliveryDto;
import com.sap.integration.anywhere.dto.AnwSalesDeliveryLineDto;
import com.sap.integration.anywhere.dto.AnwSalesOrderDto;
import com.sap.integration.anywhere.dto.AnwSkuDto;
import com.sap.integration.anywhere.dto.enumeration.AnwSalesDeliveryStatusDto;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dto.ErpSalesDeliveryDto;
import com.sap.integration.erp.dto.ErpSalesDeliveryLineDto;
import com.sap.integration.erp.dto.ErpSalesOrderDto;

/**
 * Class for transformation of SAP Anywhere's Sales Delivery to custom solution object
 */
public class SalesDeliveryTransformation {

    /**
     * Method for transformation SAP Anywhere's Sales Delivery DTO object into ERP's Sales Delivery DTO object
     * 
     * @param anwSd SAP Anywhere's Sales Delivery DTO object
     * @return ERP's Sales Delivery object
     */
    public static ErpSalesDeliveryDto run(AnwSalesDeliveryDto anwSd) {
        ErpSalesDeliveryDto erpSd = new ErpSalesDeliveryDto();
        erpSd.setAnwId(anwSd.getId());
        erpSd.setDocNumber(anwSd.getDocNumber());
        erpSd.setLastUpdateTime(anwSd.getUpdateTime());
        erpSd.setSalesOrderNumber(anwSd.getSalesOrderNumber());
        erpSd.setStatus(anwSd.getStatus().toString());

        ErpCustomerDto customer = new ErpCustomerDto();
        customer.setName((anwSd.getCustomer() != null) ? anwSd.getCustomer().getName() : "");
        customer.setCode((anwSd.getCustomer() != null) ? anwSd.getCustomer().getCode() : "");
        customer.setId((anwSd.getCustomer() != null) ? anwSd.getCustomer().getId() : null);
        erpSd.setCustomer(customer);

        if (anwSd.getLines() != null) {
            ArrayList<ErpSalesDeliveryLineDto> erpProductLines = new ArrayList<ErpSalesDeliveryLineDto>();
            for (AnwSalesDeliveryLineDto anwSdLine : anwSd.getLines()) {
                ErpSalesDeliveryLineDto erpLine = new ErpSalesDeliveryLineDto();

                erpLine.setId(anwSdLine.getId());
                erpLine.setSalesDelivery(erpSd);

                ErpSalesOrderDto salesOrder = new ErpSalesOrderDto();
                salesOrder.setAnwId(anwSdLine.getBaseDocument().getId());
                erpLine.setBaseDocument(salesOrder);

                erpLine.setDeliveryQuantity(anwSdLine.getDeliveryQuantity());
                erpLine.setRequiredQuantity(anwSdLine.getRequiredQuantity());

                ErpProductDto erpProduct = new ErpProductDto();
                erpProduct.setId((anwSdLine.getSku() != null) ? new Long(anwSdLine.getSku().getId().longValue()) : null);
                erpProduct.setItemCode((anwSdLine.getSku() != null) ? anwSdLine.getSku().getCode() : "");
                erpProduct.setItemName((anwSdLine.getSku() != null) ? anwSdLine.getSku().getName() : "");
                erpLine.setSku(erpProduct);

                erpProductLines.add(erpLine);
            }

            erpSd.setLines(erpProductLines);

        } else {
            erpSd.setLines(null);
        }

        return erpSd;
    }

    /**
     * Method for transformation ERP's Invoice DTO object into SAP Anywhere's Invoice DTO object
     * 
     * @param erpSd ERP's Invoice object
     * @return SAP Anywhere's Invoice DTO object
     */
    public static AnwSalesDeliveryDto run(ErpSalesDeliveryDto erpSd) {
        AnwSalesDeliveryDto anwSd = new AnwSalesDeliveryDto();
        anwSd.setDocNumber(erpSd.getDocNumber());
        anwSd.setUpdateTime(erpSd.getLastUpdateTime());
        anwSd.setSalesOrderNumber(erpSd.getSalesOrderNumber());
        anwSd.setStatus(AnwSalesDeliveryStatusDto.valueOf(erpSd.getStatus().toUpperCase()));

        AnwMarketingDocumentCustomerDto customer = new AnwMarketingDocumentCustomerDto();
        customer.setName((erpSd.getCustomer() != null) ? erpSd.getCustomer().getName() : "");
        customer.setCode((erpSd.getCustomer() != null) ? erpSd.getCustomer().getCode() : "");
        customer.setId((erpSd.getCustomer() != null) ? erpSd.getCustomer().getId() : null);
        anwSd.setCustomer(customer);

        if (erpSd.getLines() != null) {
            ArrayList<AnwSalesDeliveryLineDto> anwProductLines = new ArrayList<AnwSalesDeliveryLineDto>();
            for (ErpSalesDeliveryLineDto erpSdLine : erpSd.getLines()) {
                AnwSalesDeliveryLineDto anwLine = new AnwSalesDeliveryLineDto();

                anwLine.setId(erpSdLine.getId());

                AnwSalesOrderDto salesOrder = new AnwSalesOrderDto();
                anwLine.setBaseDocument(salesOrder);

                anwLine.setDeliveryQuantity(erpSdLine.getDeliveryQuantity());
                anwLine.setRequiredQuantity(erpSdLine.getRequiredQuantity());

                AnwSkuDto anwSku = new AnwSkuDto();
                anwSku.setId((erpSdLine.getSku() != null) ? (Long) erpSdLine.getSku().getId() : null);
                anwSku.setCode((erpSdLine.getSku() != null) ? erpSdLine.getSku().getItemCode() : "");
                anwSku.setName((erpSdLine.getSku() != null) ? erpSdLine.getSku().getItemName() : "");
                anwLine.setSku(anwSku);

                anwProductLines.add(anwLine);
            }

            anwSd.setLines(anwProductLines);

        } else {
            anwSd.setLines(null);
        }

        return anwSd;
    }
}