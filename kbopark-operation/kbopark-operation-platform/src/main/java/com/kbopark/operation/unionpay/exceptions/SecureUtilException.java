package com.kbopark.operation.unionpay.exceptions;

public class SecureUtilException extends Exception {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SecureUtilException(String message){
        this.message = message;
    }


}
