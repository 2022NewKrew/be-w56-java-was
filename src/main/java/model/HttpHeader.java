package model;

import java.util.Arrays;

public enum HttpHeader {
    ACCEPT("Accept"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    HOST("Host"),
    CONNECTION("Connection"),
    USER_AGENT("User-Agent"),
    SEC_CH_UA("sec-ch-ua"),
    SEC_CH_UA_MOBILE("sec-ch-ua-mobile"),
    SEC_CH_UA_PLATFORM("sec-ch-ua-platform"),
    SEC_FETCH_SITE("Sec-Fetch-Site"),
    SEC_FETCH_MODE("Sec-Fetch-Mode"),
    SEC_FETCH_DEST("Sec-Fetch-Dest"),
    REFERER("Referer"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    COOKIE("Cookie"),
    SET_COOKIE("Set-Cookie"),
    SEC_FETCH_USER("Sec-Fetch-User"),
    CACHE_CONTROL("Cache-Control"),
    UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
    LOCATION("Location"),
    DEFAULT("");

    public static HttpHeader of(String value) {
        return Arrays.stream(HttpHeader.values())
                .filter(str -> str.getValue().equals(value))
                .findAny()
                .orElse(DEFAULT);
    }

    private final String value;

    HttpHeader(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
