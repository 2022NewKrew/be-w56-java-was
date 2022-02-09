package webserver.http;

import java.util.HashMap;
import java.util.Map;

public enum Header {

    // GENERAL HEADER
    DATE("Date"),
    CACHE_CONTROL("Cache-Control"),
    CONNECTION("Connection"),

    // REQUEST HEADER
    HOST("Host"),
    COOKIE("Cookie"),
    USER_AGENT("User-Agent"),
    ACCEPT("Accept"),
    ACCEPT_LANGUAGE("Accept-Language"),
    ACCEPT_ENCODING("Accept-Encoding"),
    REFERER("Referer"),
    UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
    IF_MODIFIED_SINCE("If-Modified-Since"),
    IF_NONE_MATCH("If-None-Match"),

    // RESPONSE HEADER
    LOCATION("Location"),
    CONTENT_TYPE("Content-Type"),
    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
    ETAG("Etag"),
    KEEP_ALIVE("Keep-Alive"),
    LAST_MODIFIED("Last-Modified"),
    SERVER("Server"),
    SET_COOKIE("Set-Cookie"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    VARY("Vary"),
    X_BACKEND_SERVER("X-Backend-Server"),
    X_CACHE_INFO("X-Cache-Info"),
    X_KUMA_REVISION("X-Kuma-Revision"),
    X_FRAME_OPTION9("X-Frame-Options"),

    // ENTITY HEADER
    CONTENT_LENGTH("Content-Length"),
    CONTENT_LANGUAGE("Content-Language"),
    CONTENT_ENCODING("Content-Encoding"),

    NONE("");

    private static final Map<String, Header> headerMap = new HashMap<>();

    private final String key;

    Header(String key) {
        this.key = key;
    }

    static {
        for (Header header : values()) {
            headerMap.put(header.key, header);
        }
    }

    public static Header from(String key) {
        return headerMap.getOrDefault(key, NONE);
    }

    @Override
    public String toString() {
        return key;
    }
}
