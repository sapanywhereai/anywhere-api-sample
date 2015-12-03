package com.sap.integration.erp.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Invoice Line data transfer object used when simulating communication with ERP. This class can be adjusted to reflect your
 * solution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErpInvoiceLineDto {

    private Long id;
    private ErpInvoiceDto invoice;
    private ErpSalesOrderDto baseDocument;
    private Integer quantity;
    private Double lineTotal;
    private Double unitPrice;
    private ErpProductDto sku;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ErpInvoiceDto getInvoice() {
        return this.invoice;
    }

    public void setInvoice(ErpInvoiceDto invoice) {
        this.invoice = invoice;
    }

    public ErpSalesOrderDto getBaseDocument() {
        return this.baseDocument;
    }

    public void setBaseDocument(ErpSalesOrderDto baseDocument) {
        this.baseDocument = baseDocument;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

	public Double getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(Double lineTotal) {
		this.lineTotal = lineTotal;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
    public ErpProductDto getSku() {
        return this.sku;
    }

    public void setSku(ErpProductDto sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", invoice=");
        sb.append(this.invoice);
        sb.append(", baseDocument=");
        sb.append(this.baseDocument);
        sb.append(", quantity=");
        sb.append(this.quantity);
        sb.append(", lineTotal=");
        sb.append(this.lineTotal);
        sb.append(", unitPrice=");
        sb.append(this.unitPrice);
        sb.append(", sku=");
        sb.append(this.sku);
        sb.append("]");
        return sb.toString();
    }
}
