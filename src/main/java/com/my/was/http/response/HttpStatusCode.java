package com.my.was.http.response;

public enum HttpStatusCode {
    OK(200,"OK"), NOT_FOUND(404,"NOT_FOUND"), FOUND(302, "FOUND"),;

    private int statusCode;
    private String statusPhrase;

    HttpStatusCode(int statusCode, String statusPhrase) {
        this.statusCode = statusCode;
        this.statusPhrase = statusPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusPhrase() {
        return statusPhrase;
    }
}
