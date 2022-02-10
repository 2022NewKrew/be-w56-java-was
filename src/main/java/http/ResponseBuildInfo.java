package http;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuildInfo {

    private String path;
    private boolean isRedirect;
    private Map<String, String> cookie = new HashMap<>();
    private byte[] body;

    public ResponseBuildInfo() {
        isRedirect = false;
    }

    public String getPath() {
        return path;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public Map<String, String> getCookie() {
        return cookie;
    }

    public byte[] getBody() {
        return body;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setRedirect(boolean redirect) {
        isRedirect = redirect;
    }

    public void setCookie(Map<String, String> cookie) {
        this.cookie = cookie;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
