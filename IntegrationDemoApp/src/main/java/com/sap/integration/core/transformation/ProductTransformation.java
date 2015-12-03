package com.sap.integration.core.transformation;

import com.sap.integration.anywhere.dto.AnwProductDto;
import com.sap.integration.anywhere.dto.IAnwDto;
import com.sap.integration.erp.dto.ErpProductDto;
import com.sap.integration.erp.dto.IErpDto;

/**
 * Class used for transform SAP Anywhere / ERP product data transfer object
 */
public class ProductTransformation {

    /**
     * Transform ERP to SAP Anywhere product data transfer object.
     * 
     * @param anwDto Sap Anywhere product data transfer object
     * @return IErpDto
     */
    public static IErpDto run(IAnwDto anwDto) {
        AnwProductDto anwProduct = (AnwProductDto) anwDto;
        if (anwProduct == null) {
            return null;
        }

        ErpProductDto erpProduct = new ErpProductDto();
        erpProduct.setItemCode(anwProduct.getCode());
        erpProduct.setItemName(anwProduct.getName());

        return erpProduct;
    }

    /**
     * Transform SAP Anywhere to ERP product data transfer object.
     * 
     * @param erpDto ERP product data transfer object
     * @return IAnwDto
     */
    public static IAnwDto run(IErpDto erpDto) {
        ErpProductDto erpProduct = (ErpProductDto) erpDto;
        if (erpProduct == null) {
            return null;
        }

        AnwProductDto anwProduct = new AnwProductDto();
        anwProduct.setCode(erpProduct.getItemCode());
        anwProduct.setName(erpProduct.getItemName());

        return anwProduct;
    }

}
