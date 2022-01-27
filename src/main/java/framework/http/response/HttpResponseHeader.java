package framework.http.response;

import framework.http.HttpHeader;
import framework.http.enums.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader extends HttpHeader {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseHeader.class);

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String AGE = "age";
    public static final String ALLOW = "Allow";
    public static final String EXPIRES = "Expires";
    public static final String ETAG = "Etag";
    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String SERVER = "Server";

    private Map<String, String> headers;

    public HttpResponseHeader() {
        headers = new HashMap<>();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setContentType(MediaType contentType) {
        headers.put(CONTENT_TYPE, contentType.getText());
    }

    public void setContentLength(int contentLength) {
        headers.put(CONTENT_LENGTH, String.valueOf(contentLength));
    }

    public void setLocation(String location) {
        headers.put(LOCATION, location);
    }

    public void setCookie(String cookie) {
        headers.put(SET_COOKIE, cookie);
    }

    public void setDate(String date) {
        headers.put(DATE, date);
    }

    public void setConnection(String connection) {
        headers.put(CONNECTION, connection);
    }

    public void setCacheControl(String cacheControl) {
        headers.put(CACHE_CONTROL, cacheControl);
    }

    public void setContentLanguage(String contentLanguage) {
        headers.put(CONTENT_LANGUAGE, contentLanguage);
    }

    public void setContentEncoding(String contentEncoding) {
        headers.put(CONTENT_ENCODING, contentEncoding);
    }

    public void setContentSecurityPolicy(String contentSecurityPolicy) {
        headers.put(CONTENT_SECURITY_POLICY, contentSecurityPolicy);
    }

    public void setContentDisposition(String contentDisposition) {
        headers.put(CONTENT_DISPOSITION, contentDisposition);
    }

    public void setServer(String server) {
        headers.put(SERVER, server);
    }

    public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
        headers.put(ACCESS_CONTROL_ALLOW_ORIGIN, accessControlAllowOrigin);
    }

    public void setExpires(String expires) {
        headers.put(EXPIRES, expires);
    }

    public void setAge(String age) {
        headers.put(AGE, age);
    }

    public void setEtag(String etag) {
        headers.put(ETAG, etag);
    }

    public void setAllow(String allow) {
        headers.put(ALLOW, allow);
    }

    public void setKeepAlive(String keepAlive) {
        headers.put(KEEP_ALIVE, keepAlive);
    }

    @Override
    public String getValue(String key) {
        return headers.get(key);
    }

    public String getContentType() {
        return headers.get(CONTENT_TYPE);
    }

    public String getContentLength() {
        return headers.get(CONTENT_LENGTH);
    }

    public String getLocation() {
        return headers.get(LOCATION);
    }

    public String getCookie() {
        return headers.get(SET_COOKIE);
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

    public String getContentLanguage() {
        return headers.get(CONTENT_LANGUAGE);
    }

    public String getContentEncoding() {
        return headers.get(CONTENT_ENCODING);
    }

    public String getServer() {
        return headers.get(SERVER);
    }

    public String getAccessControlAllowOrigin() {
        return headers.get(ACCESS_CONTROL_ALLOW_ORIGIN);
    }

    public String getExpires() {
        return headers.get(EXPIRES);
    }

    public String getAge() {
        return headers.get(AGE);
    }

    public String getEtag() {
        return headers.get(ETAG);
    }

    public String getAllow() {
        return headers.get(ALLOW);
    }

    public String getKeepAlive() {
        return headers.get(KEEP_ALIVE);
    }
}
