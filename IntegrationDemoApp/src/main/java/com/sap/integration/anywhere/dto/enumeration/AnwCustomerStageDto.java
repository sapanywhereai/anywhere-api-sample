package com.sap.integration.anywhere.dto.enumeration;

import java.io.Serializable;

/**
 * Enumeration for Customer Stage used by JSON for communication with SAP Anywhere.
 */
public enum AnwCustomerStageDto implements Serializable {
    SUSPECT,
    PROSPECT,
    CUSTOMER
}
