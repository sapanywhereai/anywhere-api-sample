package com.sap.integration.core.transformation;

import com.sap.integration.anywhere.dto.AnwCustomerDto;
import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.anywhere.dto.enumeration.AnwCustomerStageDto;
import com.sap.integration.anywhere.dto.enumeration.AnwCustomerStatusDto;
import com.sap.integration.anywhere.dto.enumeration.AnwCustomerTypeDto;
import com.sap.integration.anywhere.dto.enumeration.AnwMarketingStatusDto;
import com.sap.integration.erp.dto.ErpCustomerDto;
import com.sap.integration.erp.dto.IErpDto;

/**
 * Class used for transform SAP Anywhere / ERP customer data transfer object
 */
public class CustomerTransformation {

    public static final AnwCustomerTypeDto DEFAULT_CUSTOMER_TYPE = AnwCustomerTypeDto.INDIVIDUAL_CUSTOMER;
    public static final AnwCustomerStageDto DEFAULT_CUSTOMER_STAGE = AnwCustomerStageDto.CUSTOMER;
    public static final AnwCustomerStatusDto DEFAULT_CUSTOMER_STATUS = AnwCustomerStatusDto.ACTIVE;
    public static final AnwMarketingStatusDto DEFAULT_CUSTOMER_MARGINAL_STATUS = AnwMarketingStatusDto.UNKNOWN;

    /**
     * Transform SAP Anywhere to ERP customer data transfer object.
     * 
     * @param anwDto Sap Anywhere customer data transfer object
     * @return IErpDto
     */
    public static IErpDto run(IAnwDto anwDto) {
        AnwCustomerDto anwCustomer = (AnwCustomerDto) anwDto;
        if (anwCustomer == null) {
            return null;
        }

        ErpCustomerDto erpCustomer = new ErpCustomerDto();
        erpCustomer.setName(anwCustomer.getCustomerName());
        erpCustomer.setCode(anwCustomer.getCustomerCode());

        return erpCustomer;
    }

    /**
     * Transform ERP to SAP Anywhere customer data transfer object.
     * 
     * @param erpDto ERP customer data transfer object
     * @return IAnwDto
     */
    public static IAnwDto run(IErpDto erpDto) {
        ErpCustomerDto erpCustomer = (ErpCustomerDto) erpDto;
        if (erpCustomer == null) {
            return null;
        }

        AnwCustomerDto anwCustomer = new AnwCustomerDto();
        anwCustomer.setCustomerName(erpCustomer.getName());
        anwCustomer.setCustomerCode(erpCustomer.getCode());
        anwCustomer.setCustomerType(DEFAULT_CUSTOMER_TYPE);
        anwCustomer.setStage(DEFAULT_CUSTOMER_STAGE);
        anwCustomer.setStatus(DEFAULT_CUSTOMER_STATUS);
        anwCustomer.setMarketingStatus(DEFAULT_CUSTOMER_MARGINAL_STATUS);

        return anwCustomer;
    }

}
