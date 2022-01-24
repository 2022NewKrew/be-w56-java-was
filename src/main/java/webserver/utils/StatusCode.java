package webserver.utils;

import java.util.Arrays;

public enum StatusCode {
    OK("OK", 200),
    SEE_OTHER("See Other", 302),
    BAD_REQUEST("Bad Request", 400),
    NOT_FOUND("Not Found", 404),
    FORBIDDEN("Forbidden", 403),
    INTERNAL_SERVER_ERROR("Internal Server Error", 500),
    BAD_GATEWAY("Bad Gateway", 502),
    SERVICE_UNAVAILABLE("Service Unavailable", 503);

    private final String statusCodeStr;
    private final int statusCodeNumber;

    StatusCode(String statusCodeStr, int statusCodeNumber) {
        this.statusCodeStr = statusCodeStr;
        this.statusCodeNumber = statusCodeNumber;
    }

    public static StatusCode valueOfByStatusCodeStr(String statusCodeStr) {
        return Arrays.stream(values())
                .filter(statusCode -> statusCode.statusCodeStr.equals(statusCodeStr))
                .findFirst()
                .orElse(INTERNAL_SERVER_ERROR);
    }

    public static StatusCode valueOfByStatusCodeNumber(int statusCodeNumber) {
        return Arrays.stream(values())
                .filter(statusCode -> statusCode.statusCodeNumber == statusCodeNumber)
                .findFirst()
                .orElse(INTERNAL_SERVER_ERROR);
    }

    @Override
    public String toString() {
        return statusCodeStr + " " + statusCodeNumber + "\r\n";
    }


}
