package com.example.pouch.client.lib;

public class ApiException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    public ApiException(int statusCode, String responseBody) {
        super("HTTP " + statusCode + ": " + responseBody);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
