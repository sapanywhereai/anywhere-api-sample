package com.sap.integration.anywhere.dto.enumeration;

import java.io.Serializable;

/**
 * Enumeration for Sales Order Line Calculation Base used by JSON for communication with SAP Anywhere.
 */
public enum AnwSalesOrderLineCalculationBaseDto implements Serializable {
    BY_DEFAULT,
    BY_TOTAL,
    BY_PRICEANDTOTAL,
    BY_UNITPRICE
}
