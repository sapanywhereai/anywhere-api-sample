package com.sap.integration.anywhere.dto;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Sales Order object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwSkuAllWarehouseInfoDto {

    private List<AnwSkuWarehouseInfoDto> skuWarehouseInfoList;


    public List<AnwSkuWarehouseInfoDto> getSkuWarehouseInfoList() {
        return skuWarehouseInfoList;
    }

    public void setSkuWarehouseInfoList(List<AnwSkuWarehouseInfoDto> skuWarehouseInfoList) {
        this.skuWarehouseInfoList = skuWarehouseInfoList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [skuWarehouseInfoList=");
        sb.append(this.skuWarehouseInfoList);
        sb.append("]");
        return sb.toString();
    }

}
