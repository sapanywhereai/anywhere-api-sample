package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Sales Order Line data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your
 * solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpSalesOrderLineDto {

    private Long id;
    private Long orderId;
    private Integer quantity;
    private Integer inventoryUomQuantity;
    private Double unitPrice;
    private Double lineTotal;
    private String calculationBase;
    private ErpProductDto sku;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getInventoryUomQuantity() {
        return this.inventoryUomQuantity;
    }

    public void setInventoryUomQuantity(Integer inventoryUomQuantity) {
        this.inventoryUomQuantity = inventoryUomQuantity;
    }
    
	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(Double lineTotal) {
		this.lineTotal = lineTotal;
	}

    public String getCalculationBase() {
        return this.calculationBase;
    }

    public void setCalculationBase(String calculationBase) {
        this.calculationBase = calculationBase;
    }

    public ErpProductDto getSku() {
        return this.sku;
    }

    public void setSku(ErpProductDto sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(" [id=");
        builder.append(this.id);
        builder.append(", orderId=");
        builder.append(this.orderId);
        builder.append(", quantity=");
        builder.append(this.quantity);
        builder.append(", inventoryUomQuantity=");
        builder.append(this.inventoryUomQuantity);
        builder.append(", unitPrice=");
        builder.append(this.unitPrice);
        builder.append(", lineTotal=");
        builder.append(this.lineTotal);
        builder.append(", calculationBase=");
        builder.append(this.calculationBase);
        builder.append(", sku=");
        builder.append(this.sku);
        builder.append("]");
        return builder.toString();
    }
}
