package http.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum StatusCode {
    OK("200 OK"),
    FOUND("302 Found"),
    BAD_REQUEST("400 Bad Request"),
    UNAUTHORIZED("401 Unauthorized"),
    NOT_FOUND("404 Not Found");

    public static final Map<String, StatusCode> statusCodeMap = new HashMap<>();

    static {
        for (StatusCode statusCode : StatusCode.values()) {
            String key = List
                    .of(statusCode.getStatus()
                            .split(" "))
                    .get(0);
            statusCodeMap.put(key, statusCode);
        }
    }

    private final String status;

    StatusCode(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
