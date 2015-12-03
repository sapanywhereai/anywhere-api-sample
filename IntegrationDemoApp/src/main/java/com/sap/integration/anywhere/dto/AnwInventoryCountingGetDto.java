package com.sap.integration.anywhere.dto;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Object Inventory Counting which is used for HTTP GET purposes. Objects for Inventory Counting are slightly different for HTTP
 * GET and HTTP POST. That is the reason why exists AnwInventoryCountingGetDto and AnwInventoryCountingPostDto separately.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwInventoryCountingGetDto {

    private Long id;
    private String docNumber;
    private Long seriesId;
    private String status;
    private AnwWarehouseDto warehouse;
    private String countedTime;
    private Long countedTotal;
    private String creationTime;
    private String creatorName;
    private String updateTime;
    private String updatorName;
    private Long ownerCode;
    private String ownerName;
    private Long version;
    private String remark;
    private List<AnwInventoryCountingGetLineDto> lines;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocNumber() {
        return this.docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }

    public Long getSeriesId() {
        return this.seriesId;
    }

    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AnwWarehouseDto getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(AnwWarehouseDto warehouse) {
        this.warehouse = warehouse;
    }

    public String getCountedTime() {
        return this.countedTime;
    }

    public void setCountedTime(String countedTime) {
        this.countedTime = countedTime;
    }

    public Long getCountedTotal() {
        return this.countedTotal;
    }

    public void setCountedTotal(Long countedTotal) {
        this.countedTotal = countedTotal;
    }

    public String getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreatorName() {
        return this.creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdatorName() {
        return this.updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public Long getOwnerCode() {
        return this.ownerCode;
    }

    public void setOwnerCode(Long ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getVersion() {
        return this.version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AnwInventoryCountingGetLineDto> getLines() {
        return this.lines;
    }

    public void setLines(List<AnwInventoryCountingGetLineDto> lines) {
        this.lines = lines;
    }

    public void addLine(AnwInventoryCountingGetLineDto line) {
        if (this.lines == null) {
            this.lines = new ArrayList<AnwInventoryCountingGetLineDto>();
        }

        this.lines.add(line);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(" [id=");
        builder.append(this.id);
        builder.append(", docNumber=");
        builder.append(this.docNumber);
        builder.append(", seriesId=");
        builder.append(this.seriesId);
        builder.append(", status=");
        builder.append(this.status);
        builder.append(", warehouse=");
        builder.append(this.warehouse);
        builder.append(", countedTime=");
        builder.append(this.countedTime);
        builder.append(", countedTotal=");
        builder.append(this.countedTotal);
        builder.append(", creationTime=");
        builder.append(this.creationTime);
        builder.append(", creatorName=");
        builder.append(this.creatorName);
        builder.append(", updateTime=");
        builder.append(this.updateTime);
        builder.append(", updatorName=");
        builder.append(this.updatorName);
        builder.append(", ownerCode=");
        builder.append(this.ownerCode);
        builder.append(", ownerName=");
        builder.append(this.ownerName);
        builder.append(", version=");
        builder.append(this.version);
        builder.append(", remark=");
        builder.append(this.remark);
        builder.append(", lines=");
        builder.append(this.lines);
        builder.append("]");
        return builder.toString();
    }
}
