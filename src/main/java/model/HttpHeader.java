package model;

import exceptions.BadRequestFormatException;
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
    Sec_Fetch_User("Sec-Fetch-User"),
    COOwKIE("Cookie"),
    Cache_Control("Cache-Control"),
    Upgrade_Insecure_Requests("Upgrade-Insecure-Requests"),
    LOCATION("Location");

    public static HttpHeader of(String value) {
        return Arrays.stream(HttpHeader.values())
                .filter(str -> str.getValue().equals(value))
                .findAny()
                .orElseThrow(() -> new BadRequestFormatException(""));
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
