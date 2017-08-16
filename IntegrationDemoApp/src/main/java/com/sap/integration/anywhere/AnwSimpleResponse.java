package com.sap.integration.anywhere;

import java.io.IOException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import com.sap.integration.utils.JsonUtil;

/**
 * Class, which represents response of SAP Anywhere HTTP calls. It contains information about error code, url, message or parsed
 * id from HTTP POST request.
 */
public class AnwSimpleResponse extends SimpleResponse {

    private static final Logger LOG = Logger.getLogger(AnwSimpleResponse.class);

    private String errorCode;
    private String message;
    private Long id;
    private String url;

    public AnwSimpleResponse() {
    }

    public AnwSimpleResponse(final String url) {
        super();
        setUrl(url);
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isError() {
        return getErrorCode() != null || (getStatusCode() != HttpStatus.SC_CREATED && getStatusCode() != HttpStatus.SC_OK);
    }

    public boolean hasId() {
        return getId() != null;
    }

    /**
     * Method, which parse HTTP response. It determines error code, message or id in HTTP POST request.
     */
    @Override
    public void setResponse(final HttpResponse httpResponse) {
        super.setResponse(httpResponse);
        if (getContent() != null) {
            if (getContent().contains("\"errorCode\"")) {
                try {
                    AnwSimpleResponse errorResponse = JsonUtil.getObject(getContent(), AnwSimpleResponse.class);
                    this.setErrorCode(errorResponse.getErrorCode());
                    this.setMessage(errorResponse.getMessage());
                } catch (IOException e) {
                    LOG.error("IOException " + e.getMessage());
                    LOG.error("Error parsing SAP Anywhere Error: " + getContent() + "\n failed with: ", e);
                }
            } else {
                if (StringUtils.isNumericSpace(getContent()) && !getContent().isEmpty()) {
                    try {
                        setId(Long.parseLong(getContent()));
                    } catch (NumberFormatException e) {
                        LOG.error("NumberFormatException " + e.getMessage());
                        LOG.error("Error parsing SAP Anywhere Response: " + getContent() + "\n failed with: ", e);
                    }
                }
            }
        }
    }
}
