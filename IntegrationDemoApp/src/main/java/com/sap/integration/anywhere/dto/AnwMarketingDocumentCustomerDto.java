package com.sap.integration.anywhere.dto;


/**
 * Customer object used by JSON for communication with SAP Anywhere.
 */
public class AnwMarketingDocumentCustomerDto {

    private Long id;
    private String code;
    private String name;

    public AnwMarketingDocumentCustomerDto() {
    }

    public AnwMarketingDocumentCustomerDto(String name, String code) {
        this.setName(name);
        this.setCode(code);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [id=");
        sb.append(this.id);
        sb.append(", name=");
        sb.append(this.name);
        sb.append(", code=");
        sb.append(this.code);
        sb.append("]");
        return sb.toString();
    }

}