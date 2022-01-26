package http;

import webserver.exception.InternalServerErrorException;

import java.util.Arrays;

public enum ContentType {
    APPLICATION_JSON("application/json"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded");

    private String value;

    ContentType(String value) {
        this.value = value;
    }


    public static ContentType getContentType(String contentTypeName) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.value.equals(contentTypeName))
                .findFirst()
                .orElseThrow(InternalServerErrorException::new);
    }

    public String getValue() {
        return value;
    }
}
