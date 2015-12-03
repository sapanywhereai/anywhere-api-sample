package com.sap.integration.anywhere.oauth;

/**
 * Access Token object used by JSON for communication with SAP Anywhere.
 */
public class AccessTokenDto {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return this.token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Integer getExpires_in() {
        return this.expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append(" [access_token=");
        sb.append(this.access_token);
        sb.append(", token_type=");
        sb.append(this.token_type);
        sb.append(", refresh_token=");
        sb.append(this.refresh_token);
        sb.append(", expires_in=");
        sb.append(this.expires_in);
        sb.append(", scope=");
        sb.append(this.scope);
        sb.append("]");
        return sb.toString();
    }
}
