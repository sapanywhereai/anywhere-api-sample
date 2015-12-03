package com.sap.integration.anywhere.dto.enumeration;

import java.io.Serializable;

/**
 * Enumeration for Invoice Payment Status used by JSON for communication with SAP Anywhere.
 */
public enum AnwInvoicePaymentStatusDto implements Serializable {
    NOT_PAID,
    PARTIALLY_PAID,
    FULLY_PAID
}
