package com.sap.integration.anywhere.dto;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Object Inventory Counting which is used for HTTP POST purposes. Objects for Inventory Counting are slightly different for HTTP
 * GET and HTTP POST. That is the reason why exists AnwInventoryCountingGetDto and AnwInventoryCountingPostDto separately.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnwInventoryCountingPostDto {

    private String creationTime;
    private String creatorName;
    private List<AnwInventoryCountingPostLineDto> lines;
    private String ownerCode;
    private String status;
    private String updateTime;
    private String remark;
    private AnwWarehouseDto warehouse;

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

    public List<AnwInventoryCountingPostLineDto> getLines() {
        return this.lines;
    }

    public void setLines(List<AnwInventoryCountingPostLineDto> lines) {
        this.lines = lines;
    }

    public void addLine(AnwInventoryCountingPostLineDto line) {
        if (this.lines == null) {
            this.lines = new ArrayList<AnwInventoryCountingPostLineDto>(1);
        }

        this.lines.add(line);
    }

    public String getOwnerCode() {
        return this.ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public AnwWarehouseDto getWarehouse() {
        return this.warehouse;
    }

    public void setWarehouse(AnwWarehouseDto warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [creationTime=");
        sb.append(this.creationTime);
        sb.append(", creatorName=");
        sb.append(this.creatorName);
        sb.append(", lines=");
        sb.append(this.lines);
        sb.append(", ownerCode=");
        sb.append(this.ownerCode);
        sb.append(", status=");
        sb.append(this.status);
        sb.append(", updateTime=");
        sb.append(this.updateTime);
        sb.append(", remark=");
        sb.append(this.remark);
        sb.append(", warehouse=");
        sb.append(this.warehouse);
        sb.append("]");
        return sb.toString();
    }
}
