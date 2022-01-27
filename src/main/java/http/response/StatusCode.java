package http.response;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum StatusCode {
    OK("200 OK"),
    FOUND("302 Found"),
    NOT_FOUND("404 Not Found"),
    UNAUTHORIZED("401 UNAUTHORIZED");

    public static final Map<String, StatusCode> statusCodeMap = new ConcurrentHashMap<>();

    static {
        for (StatusCode statusCode : StatusCode.values()) {
            String key = List
                    .of(statusCode.getStatus()
                            .split(" "))
                    .get(0);
            statusCodeMap.put(key, statusCode);
        }
    }

    public static StatusCode getStatusString(String statusNumber) {
        return statusCodeMap.getOrDefault(statusNumber, NOT_FOUND);
    }

    private final String status;

    StatusCode(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
