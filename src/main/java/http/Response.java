package http;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private static final String HTTP_RESPONSE_200 = "HTTP/1.1 200 OK ";
    private static final String HTTP_RESPONSE_302 = "HTTP/1.1 302 OK ";

    private final Map<String, Collection> attributes = new HashMap<>();
    private final Cookie cookie = new Cookie();

    private String type = HTTP_RESPONSE_200;

    public Response() {}

    public void setCookie(String key, String value) {
        this.cookie.set(key, value);
    }

    public void setRedirect() {
        type = HTTP_RESPONSE_302;
    }

    public void addAttribute(String key, Collection attribute) {
        attributes.put(key, attribute);
    }

    public Collection getAttributes(String key) {
        return attributes.get(key);
    }

    public Cookie getCookie() {
        return cookie;
    }

    public String getType() {
        return type;
    }
}
