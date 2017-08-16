package com.sap.integration.anywhere;

public class AnwErrorObject {
    private String errorCode;

    private String message;

    public AnwErrorObject() {

    }

    public AnwErrorObject(String errorCode, String message) {
        this.setErrorCode(errorCode);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
