package com.sap.integration.customer.model;

import java.io.Serializable;

/**
 * Enumeration for Customer Type used by JSON for communication with SAP Anywhere.
 */
public enum AnwCustomerTypeDto implements Serializable {
    CORPORATE_CUSTOMER,
    INDIVIDUAL_CUSTOMER
}
