package com.sap.integration.anywhere.dto.enumeration;

import java.io.Serializable;

/**
 * Enumeration for Marketing Status used by JSON for communication with SAP Anywhere.
 */
public enum AnwMarketingStatusDto implements Serializable {
    SUBSCRIBED,
    UNSUBSCRIBED,
    CLEANED,
    UNKNOWN
}
