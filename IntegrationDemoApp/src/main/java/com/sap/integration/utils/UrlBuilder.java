package com.sap.integration.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Logger;

/**
 * Class, which dynamically creates URL, encodes query parameters by HTTP encoding, converts string URL to various type of
 * objects.
 */
public class UrlBuilder {

    private static final Logger LOG = Logger.getLogger(UrlBuilder.class);

    /** Variable, which stores information about path. */
    private StringBuilder urlPath = new StringBuilder();

    /** Variable, which stores information about query parameters. */
    private StringBuilder query = new StringBuilder();

    public UrlBuilder() {
    }

    public UrlBuilder(final String defaultUrl) {
        this.urlPath.append(defaultUrl);
    }

    public UrlBuilder(final UrlBuilder other) {
        this.urlPath.append(other.urlPath);
        this.query.append(other.query);
    }

    /**
     * Method append value to "path" part of URL. It is not append to "query" part of URL.
     * 
     * @param value - value which will be appended
     * @return this - instance of UrlBuilder
     */
    public UrlBuilder append(final String value) {
        this.urlPath.append(value);
        return this;
    }

    /**
     * Method appends query parameter with value to query part of URL. Method automatically determines, whether parameter is first
     * or not and use ? or &amp; for concatenation of query parameters and values. It also encodes entered value (not parameter!).
     * 
     * @param parameter
     * @param value
     * @return this - instance of UrlBuilder
     */
    public UrlBuilder parameter(final String parameter, final Number value) {
        this.parameter(parameter, value.toString());
        return this;
    }

    /**
     * Method appends query parameter with value to query part of URL. Method automatically determines, whether parameter is first
     * or not and use ? or &amp; for concatenation of query parameters and values. It also encodes entered value (not parameter!).
     * 
     * @param parameter
     * @param value
     * @return this - instance of UrlBuilder
     */
    public UrlBuilder parameter(final String parameter, final String value) {
        if (this.query.length() == 0) {
            this.query.append("?");
        } else {
            this.query.append("&");
        }
        this.query.append(parameter).append("=");
        try {
            this.query.append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            LOG.warn("UnsupportedEncodingException: " + e.getMessage());
            query.append(value);
        }
        return this;
    }

    /**
     * Method, which returns created URL as object of URL.
     * 
     * @return URL as instance of URL
     * @throws MalformedURLException
     */
    public URL getURL() throws MalformedURLException {
        return new URL(this.get());
    }

    /**
     * Method, which returns created URL as object of URI.
     * 
     * @return URL as instance of URI
     * @throws MalformedURLException
     */
    public URI getURI() throws URISyntaxException {
        return new URI(this.get());
    }

    /**
     * Method, which returns created URL as object of String.
     * 
     * @return URL as instance of String
     */
    public String get() {
        return new StringBuilder(this.urlPath).append(this.query).toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [url=");
        sb.append(this.get());
        sb.append("]");
        return sb.toString();
    }
}
