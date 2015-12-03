package com.sap.integration.anywhere.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Invoice Line object used by JSON for communication with SAP Anywhere.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwInvoiceLineDto {

    private Long id;
    private AnwSalesOrderDto baseDocument;
    private Integer quantity;
    private AnwAmountDto grossAmount;
    private Double grossUnitPrice;
    private AnwSkuDto sku;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnwSalesOrderDto getBaseDocument() {
        return this.baseDocument;
    }

    public void setBaseDocument(AnwSalesOrderDto baseDocument) {
        this.baseDocument = baseDocument;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

	public AnwAmountDto getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(AnwAmountDto grossAmount) {
		this.grossAmount = grossAmount;
	}

	public Double getGrossUnitPrice() {
		return grossUnitPrice;
	}

	public void setGrossUnitPrice(Double grossUnitPrice) {
		this.grossUnitPrice = grossUnitPrice;
	}
	
    public AnwSkuDto getSku() {
        return this.sku;
    }

    public void setSku(AnwSkuDto sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", baseDocument=");
        sb.append(this.baseDocument);
        sb.append(", quantity=");
        sb.append(this.quantity);
        sb.append(", grossAmount=");
        sb.append(this.grossAmount);
        sb.append(", grossUnitPrice=");
        sb.append(this.grossUnitPrice);
        sb.append(", sku=");
        sb.append(this.sku);
        sb.append("]");
        return sb.toString();
    }
}