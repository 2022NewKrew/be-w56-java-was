package http;

import util.HttpRequestUtils;

import java.util.Map;

public class HttpRequest {

    private Method method;
    private String url;
    private Protocol protocol;
    private Header header = new Header();

    public String getMethod() {
        return method.getName();
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol.getName();
    }

    public Header getHeader() {
        return header;
    }

    public void setStartLine(String method, String url, String protocol) {
        setMethod(method);
        setUrl(url);
        setProtocol(protocol);
    }

    private void setMethod(String method) {
        this.method = Method.valueOf(method);
    }

    private void setUrl(String url) {
        this.url = url;
    }

    private void setProtocol(String protocol) {
        for (Protocol p : Protocol.values()) {
            if (p.getName().equals(protocol)) {
                this.protocol = p;
            }
        }
    }

    public void setHeaderValue(String key, String value) {
        this.header.setValue(key, value);
    }

    private enum Method {
        GET("GET"), POST("POST"), DELETE("DELETE");
        private final String name;
        Method(String name) {
            this.name = name;
        }
        private String getName() {
            return this.name;
        }
    }

    private enum Protocol {
        HTTP_1_0("HTTP/1.0"), HTTP_1_1("HTTP/1.1"), HTTP_2_0("HTTP/2.0"), HTTPS("HTTPS");
        private final String name;
        Protocol(String name) {
            this.name = name;
        }
        private String getName() {
            return this.name;
        }
    }

}
