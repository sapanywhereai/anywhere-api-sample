package com.sap.integration.utils;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Class, which represents and store information about HTTP response.
 */
public class SimpleResponse {

    private static final Logger LOG = Logger.getLogger(SimpleResponse.class);

    private int statusCode;

    private String content;

    public SimpleResponse() {
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean hasContent() {
        return StringUtils.isNotBlank(getContent());
    }

    /**
     * Method, which parse HTTP response and set up status code and content of response.
     * 
     * @param httpResponse - HTTP response from which will be parsed status code and content
     */
    public void setResponse(final HttpResponse httpResponse) {
        if (httpResponse != null) {
            if (httpResponse.getStatusLine() != null) {
                this.statusCode = httpResponse.getStatusLine().getStatusCode();
            }
            if (httpResponse.getEntity() != null) {
                try {
                    this.content = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8.name());
                } catch (Exception e) {
                    LOG.error("Exception: " + e.getMessage());
                }
            }
        }
    }

    /**
     * String representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [statusCode=");
        sb.append(this.statusCode);
        sb.append(", content=");
        sb.append(this.content);
        sb.append("]");
        return sb.toString();
    }
}
