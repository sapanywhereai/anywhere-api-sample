package com.sap.integration.anywhere.dto.enumeration;

import java.io.Serializable;

/**
 * Enumeration for Payment Status used by JSON for communication with SAP Anywhere.
 */
public enum AnwPaymentStatusDto implements Serializable {
    OPEN,
    CLOSE,
    CANCEL
}
