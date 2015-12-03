package com.sap.integration.anywhere.dto.enumeration;

import java.io.Serializable;

/**
 * Enumeration for Customer Status used by JSON for communication with SAP Anywhere.
 */
public enum AnwCustomerStatusDto implements Serializable {
    ACTIVE,
    INACTIVE,
    DUPLICATED
}
