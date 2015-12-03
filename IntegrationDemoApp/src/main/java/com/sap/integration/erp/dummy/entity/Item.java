package com.sap.integration.erp.dummy.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;

import com.sap.integration.erp.dto.IErpDto;
import com.sap.integration.erp.dummy.conversion.ProductConversion;
import com.sap.integration.erp.dummy.jpa.JpaLayer;

/**
 * Item ERP database entity used by database layer of ERP. This class can be adjusted to reflect your solution.
 */
@Entity
public class Item extends JpaLayer implements IErpDB, Serializable {

    private static final long serialVersionUID = -6453719614780593237L;

    private String itemCode;

    private String itemName;

    public Item() {
        super();
    }

    public String getItemCode() {
        return this.itemCode;
    }

    public Item setItemCode(String itemCode) {
        this.itemCode = itemCode;
        return this;
    }

    public String getItemName() {
        return this.itemName;
    }

    public Item setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public IErpDto convert() {
        return ProductConversion.run(this);
    }

    public static <O, I> List<O> convert(List<I> itemsToConvert, List<O> toListType) {
        return ProductConversion.run(itemsToConvert, toListType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [itemCode=");
        sb.append(this.itemCode);
        sb.append(", itemName=");
        sb.append(this.itemName);
        sb.append("]");
        return sb.toString();
    }
}
