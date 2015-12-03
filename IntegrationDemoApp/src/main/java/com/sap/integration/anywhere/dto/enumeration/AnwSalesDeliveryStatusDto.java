package com.sap.integration.anywhere.dto.enumeration;

import java.io.Serializable;

/**
 * Enumeration for Sales Delivery Status used by JSON for communication with SAP Anywhere.
 */
public enum AnwSalesDeliveryStatusDto implements Serializable {
    OPEN,
    CLOSED,
    PICK,
    PACK,
    SHIPPING,
    PENDING,
    CANCELLED
}
