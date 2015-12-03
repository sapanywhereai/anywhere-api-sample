package com.sap.integration.core.transformation;

import java.util.ArrayList;

import com.sap.integration.anywhere.dto.AnwAmountDto;
import com.sap.integration.anywhere.dto.AnwInvoiceDto;
import com.sap.integration.anywhere.dto.AnwInvoiceLineDto;
import com.sap.integration.anywhere.dto.AnwMarketingDocumentCustomerDto;
import com.sap.integration.anywhere.dto.AnwSalesOrderDto;
import com.sap.integration.anywhere.dto.AnwSkuDto;
import com.sap.integration.anywhere.dto.enumeration.AnwInvoicePaymentStatusDto;
import com.sap.integration.anywhere.dto.enumeration.AnwInvoiceStatusDto;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.ErpInvoiceDto;
import com.sap.integration.erp.dto.ErpInvoiceLineDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dto.ErpSalesOrderDto;

/**
 * Class for transformation of SAP Anywhere's Invoice to custom solution object
 */
public class InvoiceTransformation {

    /**
     * Method for transformation SAP Anywhere's Invoice DTO object into ERP's Invoice DTO object
     * 
     * @param anwInv SAP Anywhere's Invoice DTO object
     * @return ERP's Invoice object
     */
    public static ErpInvoiceDto run(AnwInvoiceDto anwInv) {
        ErpInvoiceDto erpInv = new ErpInvoiceDto();
        
        erpInv.setAnwId(anwInv.getId());
        erpInv.setDocNumber(anwInv.getDocNumber());
        erpInv.setLastUpdateTime(anwInv.getUpdateTime());
        erpInv.setStatus(anwInv.getStatus().toString());
        erpInv.setPaymentStatus(anwInv.getPaymentStatus().toString());
        erpInv.setDocTotal((anwInv.getGrossTotal() != null) ? ((anwInv.getGrossTotal().getAmount() != null) ? anwInv.getGrossTotal()
                .getAmount().doubleValue() : null) : null);

        ErpCustomerDto customer = new ErpCustomerDto();
        customer.setName((anwInv.getCustomer() != null) ? anwInv.getCustomer().getName() : "");
        customer.setCode((anwInv.getCustomer() != null) ? anwInv.getCustomer().getCode() : "");
        customer.setId((anwInv.getCustomer() != null) ? anwInv.getCustomer().getId() : null);
        erpInv.setCustomer(customer);

        if (anwInv.getInvoiceLines() != null) {
            ArrayList<ErpInvoiceLineDto> erpInvoiceLines = new ArrayList<ErpInvoiceLineDto>();
            for (AnwInvoiceLineDto anwInvLine : anwInv.getInvoiceLines()) {
                ErpInvoiceLineDto erpLine = new ErpInvoiceLineDto();

                erpLine.setId(anwInvLine.getId());
                erpLine.setInvoice(erpInv);
                erpLine.setQuantity(anwInvLine.getQuantity());
                erpLine.setUnitPrice(anwInvLine.getGrossUnitPrice());
                erpLine.setLineTotal(anwInvLine.getGrossAmount().getAmount());

                ErpSalesOrderDto salesOrder = new ErpSalesOrderDto();
                salesOrder.setAnwId(anwInvLine.getBaseDocument().getId());
                erpLine.setBaseDocument(salesOrder);

                ErpProductDto erpProduct = new ErpProductDto();
                erpProduct.setId((anwInvLine.getSku() != null) ? new Long(anwInvLine.getSku().getId().longValue()) : null);
                erpProduct.setItemCode((anwInvLine.getSku() != null) ? anwInvLine.getSku().getCode() : "");
                erpProduct.setItemName((anwInvLine.getSku() != null) ? anwInvLine.getSku().getName() : "");
                erpLine.setSku(erpProduct);

                erpInvoiceLines.add(erpLine);
            }

            erpInv.setInvoiceLines(erpInvoiceLines);

        } else {
            erpInv.setInvoiceLines(null);
        }

        return erpInv;
    }

    /**
     * Method for transformation ERP's Invoice DTO object into SAP Anywhere's Invoice DTO object
     * 
     * @param erpInv ERP's Invoice object
     * @return SAP Anywhere's Invoice DTO object
     */
    public static AnwInvoiceDto run(ErpInvoiceDto erpInv) {
        AnwInvoiceDto anwInv = new AnwInvoiceDto();
        
        anwInv.setDocNumber(erpInv.getDocNumber());
        anwInv.setUpdateTime(erpInv.getLastUpdateTime());
        anwInv.setStatus(AnwInvoiceStatusDto.valueOf(erpInv.getStatus().toUpperCase()));
        
        anwInv.setPaymentStatus(AnwInvoicePaymentStatusDto.valueOf(erpInv.getPaymentStatus().toUpperCase()));

        AnwMarketingDocumentCustomerDto customer = new AnwMarketingDocumentCustomerDto();
        customer.setName((erpInv.getCustomer() != null) ? erpInv.getCustomer().getName() : "");
        customer.setCode((erpInv.getCustomer() != null) ? erpInv.getCustomer().getCode() : "");
        customer.setId((erpInv.getCustomer() != null) ? erpInv.getCustomer().getId() : null);
        anwInv.setCustomer(customer);

        if (erpInv.getInvoiceLines() != null) {
            ArrayList<AnwInvoiceLineDto> anwInvoiceLines = new ArrayList<AnwInvoiceLineDto>();
            for (ErpInvoiceLineDto erpInvLine : erpInv.getInvoiceLines()) {
                AnwInvoiceLineDto anwLine = new AnwInvoiceLineDto();

                anwLine.setId(erpInvLine.getId());
                anwLine.setQuantity(erpInvLine.getQuantity());
                anwLine.setGrossUnitPrice(erpInvLine.getUnitPrice());
                anwLine.setGrossAmount(new AnwAmountDto(erpInvLine.getLineTotal(),erpInvLine.getLineTotal()));

                if ((erpInvLine.getBaseDocument() != null) && (erpInvLine.getBaseDocument().getAnwId() != null)) {
                    AnwSalesOrderDto salesOrder = new AnwSalesOrderDto();
                    salesOrder.setId(erpInvLine.getBaseDocument().getAnwId());
                    anwLine.setBaseDocument(salesOrder);
                } else {
                    anwLine.setBaseDocument(null);
                }

                AnwSkuDto anwSku = new AnwSkuDto();
                anwSku.setId((erpInvLine.getSku() != null) ? (Long) erpInvLine.getSku().getId() : null);
                anwSku.setCode((erpInvLine.getSku() != null) ? erpInvLine.getSku().getItemCode() : "");
                anwSku.setName((erpInvLine.getSku() != null) ? erpInvLine.getSku().getItemName() : "");
                anwLine.setSku(anwSku);

                anwInvoiceLines.add(anwLine);
            }

            anwInv.setInvoiceLines(anwInvoiceLines);

        } else {
            anwInv.setInvoiceLines(null);
        }

        return anwInv;
    }
}