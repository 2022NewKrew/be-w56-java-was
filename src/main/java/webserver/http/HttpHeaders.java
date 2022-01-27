package webserver.http;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.StringJoiner;

public class HttpHeaders {

    private final Map<HttpHeader, String> httpHeaders;

    public HttpHeaders() {
        this.httpHeaders = Collections.synchronizedMap(new EnumMap<>(HttpHeader.class));
    }

    public HttpHeaders set(HttpHeader key, MimeSubtype value) {
        return set(key, value.toString());
    }

    public HttpHeaders set(HttpHeader key, String value) {
        httpHeaders.put(key, value);
        return this;
    }

    public String toHeaderString() {
        StringJoiner joiner = new StringJoiner("\r\n", "", "\r\n");
        httpHeaders.forEach((key, value) -> joiner.add(key + ": " + value));
        return joiner.toString();
    }
}
