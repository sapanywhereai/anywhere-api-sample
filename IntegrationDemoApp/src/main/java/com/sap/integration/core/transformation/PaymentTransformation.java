package com.sap.integration.core.transformation;

import java.util.ArrayList;

import com.sap.integration.anywhere.dto.AnwAmountDto;
import com.sap.integration.anywhere.dto.AnwMarketingDocumentCustomerDto;
import com.sap.integration.anywhere.dto.AnwPaymentDto;
import com.sap.integration.anywhere.dto.AnwPaymentLineDto;
import com.sap.integration.anywhere.dto.AnwTransactionDocumentDto;
import com.sap.integration.anywhere.dto.enumeration.AnwPaymentStatusDto;
import com.sap.integration.erp.dto.ErpAmountDto;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.ErpPaymentDto;
import com.sap.integration.erp.dto.ErpPaymentLineDto;
import com.sap.integration.erp.dto.ErpTransactionDocumentDto;

/**
 * Class for transformation of SAP Anywhere's Payment to custom solution object
 */
public class PaymentTransformation {

    /**
     * Method for transformation SAP Anywhere's Payment DTO object into ERP's Payment DTO object
     * 
     * @param anwPay SAP Anywhere's Payment DTO object
     * @return ERP's Payment object
     */
    public static ErpPaymentDto run(AnwPaymentDto anwPay) {
        ErpPaymentDto erpPay = new ErpPaymentDto();
        erpPay.setAnwId(anwPay.getId());
        erpPay.setDocNumber(anwPay.getDocNumber());
        erpPay.setLastUpdateTime(anwPay.getUpdateTime());
        erpPay.setStatus(anwPay.getStatus().toString());

        ErpAmountDto amount = new ErpAmountDto();
        amount.setAmount(anwPay.getAmount().getAmount());
        amount.setAmountLocalCurrency(anwPay.getAmount().getAmountLocalCurrency());
        erpPay.setAmount(amount);

        ErpCustomerDto customer = new ErpCustomerDto();
        customer.setName((anwPay.getCustomer() != null) ? anwPay.getCustomer().getName() : "");
        customer.setCode((anwPay.getCustomer() != null) ? anwPay.getCustomer().getCode() : "");
        customer.setId((anwPay.getCustomer() != null) ? anwPay.getCustomer().getId() : null);
        erpPay.setCustomer(customer);

        if (anwPay.getPaymentLines() != null) {
            ArrayList<ErpPaymentLineDto> erpPaymentLines = new ArrayList<ErpPaymentLineDto>();
            for (AnwPaymentLineDto anwPayLine : anwPay.getPaymentLines()) {
                ErpPaymentLineDto erpLine = new ErpPaymentLineDto();

                erpLine.setId(anwPayLine.getId());

                ErpTransactionDocumentDto erpTransactionDocument = new ErpTransactionDocumentDto();
                erpTransactionDocument.setId(anwPayLine.getTransactionDocument().getId());
                erpTransactionDocument.setType(anwPayLine.getTransactionDocument().getType());
                erpLine.setTransactionDocument(erpTransactionDocument);

                ErpAmountDto amnt = new ErpAmountDto();
                amnt.setAmount(anwPay.getAmount().getAmount());
                amnt.setAmountLocalCurrency(anwPay.getAmount().getAmountLocalCurrency());
                erpLine.setAppliedAmount(amnt);

                erpPaymentLines.add(erpLine);
            }

            erpPay.setPaymentLines(erpPaymentLines);

        } else {
            erpPay.setPaymentLines(null);
        }

        return erpPay;
    }

    /**
     * Method for transformation ERP's Payment DTO object into SAP Anywhere's Payment DTO object
     * 
     * @param erpPay ERP's Payment object
     * @return SAP Anywhere's Payment DTO object
     */
    public static AnwPaymentDto run(ErpPaymentDto erpPay) {
        AnwPaymentDto anwPay = new AnwPaymentDto();
        anwPay.setDocNumber(erpPay.getDocNumber());
        anwPay.setUpdateTime(erpPay.getLastUpdateTime());
        anwPay.setStatus(AnwPaymentStatusDto.valueOf(erpPay.getStatus().toUpperCase()));

        AnwMarketingDocumentCustomerDto customer = new AnwMarketingDocumentCustomerDto();
        customer.setName((erpPay.getCustomer() != null) ? erpPay.getCustomer().getName() : "");
        customer.setCode((erpPay.getCustomer() != null) ? erpPay.getCustomer().getCode() : "");
        customer.setId((erpPay.getCustomer() != null) ? erpPay.getCustomer().getId() : null);
        anwPay.setCustomer(customer);
        anwPay.setAmount(new AnwAmountDto(erpPay.getAmount().getAmount(), erpPay.getAmount().getAmountLocalCurrency()));

        if (erpPay.getPaymentLines() != null) {
            ArrayList<AnwPaymentLineDto> anwPaymentLines = new ArrayList<AnwPaymentLineDto>();
            for (ErpPaymentLineDto erpPayLine : erpPay.getPaymentLines()) {
                AnwPaymentLineDto anwLine = new AnwPaymentLineDto();

                anwLine.setId(erpPayLine.getId());

                AnwTransactionDocumentDto anwTransactionDocument = new AnwTransactionDocumentDto();
                anwTransactionDocument.setId(erpPayLine.getTransactionDocument().getId());
                anwTransactionDocument.setType(erpPayLine.getTransactionDocument().getType());
                anwLine.setTransactionDocument(anwTransactionDocument);
                anwLine.setAppliedAmount(new AnwAmountDto(erpPay.getAmount().getAmount(), erpPay.getAmount()
                        .getAmountLocalCurrency()));

                anwPaymentLines.add(anwLine);
            }

            anwPay.setPaymentLines(anwPaymentLines);

        } else {
            anwPay.setPaymentLines(null);
        }

        return anwPay;
    }
}