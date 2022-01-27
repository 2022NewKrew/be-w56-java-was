package framework.http.request;

import framework.http.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader extends HttpHeader {
    private static final Logger log = LoggerFactory.getLogger(HttpRequestHeader.class);

    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String AUTHORIZATION = "Authorization";
    public static final String COOKIE = "Cookie";
    public static final String HOST = "Host";
    public static final String ORIGIN = "Origin";
    public static final String REFERER = "Referer";
    public static final String USER_AGENT = "User-Agent";
    public static final String UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";

    private Map<String, String> headers;

    public HttpRequestHeader(BufferedReader bufferedReader) throws IOException {
        makeHeaders(bufferedReader);
    }

    private void makeHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            log.debug("header: {}", line);
            if (line.equals("")) {
                break;
            }

            String[] split = line.split(HEADER_KEY_VALUE_SPLIT_DELIMITER);
            String key = split[0];
            String value = split[1];
            headers.put(key, value);
        }

        this.headers = Collections.unmodifiableMap(headers);
    }

    @Override
    public String getValue(String key) {
        return headers.get(key);
    }

    public String getContentLength() {
        return headers.get(CONTENT_LENGTH);
    }

    public String getDate() {
        return headers.get(DATE);
    }

    public String getConnection() {
        return headers.get(CONNECTION);
    }

    public String getCacheControl() {
        return headers.get(CACHE_CONTROL);
    }

    public String getContentType() {
        return headers.get(CONTENT_TYPE);
    }

    public String getContentLanguage() {
        return headers.get(CONTENT_LANGUAGE);
    }

    public String getContentEncoding() {
        return headers.get(CONTENT_ENCODING);
    }

    public String getAccept() {
        return headers.get(ACCEPT);
    }

    public String getAcceptEncoding() {
        return headers.get(ACCEPT_ENCODING);
    }

    public String getCookie() {
        return headers.get(COOKIE);
    }

    public String getHost() {
        return headers.get(HOST);
    }

    public String getUserAgent() {
        return headers.get(USER_AGENT);
    }

    public String getUpgradeInsecureRequests() {
        return headers.get(UPGRADE_INSECURE_REQUESTS);
    }

    public String getAuthorization() {
        return headers.get(AUTHORIZATION);
    }

    public String getOrigin() {
        return headers.get(ORIGIN);
    }

    public String getReferer() {
        return headers.get(REFERER);
    }

    public String getContentSecurityPolicy() {
        return headers.get(CONTENT_SECURITY_POLICY);
    }

    public String getContentDisPosition() {
        return headers.get(CONTENT_DISPOSITION);
    }

    public String getLocation() {
        return headers.get(LOCATION);
    }
}
