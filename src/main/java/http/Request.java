package http;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Request {
    private List<String> requestHeader;
    private List<String> requestBody;
    private String path;
    private HttpMethod method;
    private Map<String, String> elements;
    private HttpCookie cookie;
    private HttpSession session;

    private Request(Builder builder){
        this.requestHeader = builder.requestHeader;
        this.requestBody = builder.requestBody;
        this.path = builder.path;
        this.method = builder.method;
        this.elements = builder.elements;
        this.cookie = builder.cookie;
        this.session = builder.session;
    }

    public List<String> getRequestHeader() {
        return requestHeader;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getElements() {
        return elements;
    }

    public List<String> getRequestBody() {
        return requestBody;
    }

    public void setCookieValue(String key, String value) {
        this.cookie.addValue(key, value);
    }

    public String getCookieValue(String key) {
        return this.cookie.getValue(key);
    }

    public void setSessionValue(String key, Object value){
        this.session.setValue(key, value);
    }

    public Object getSessionValue(String key){
        return session.getValue(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String line : requestHeader){
            sb.append(line + "\n");
        }
        return sb.toString();
    }

    public static class Builder {
        private List<String> requestHeader;
        private List<String> requestBody;
        private String path;
        private HttpMethod method;
        private Map<String, String> elements;
        private HttpCookie cookie;
        private HttpSession session;


        public Builder requestHeader(List<String> requestHeader) {
            this.requestHeader = requestHeader;
            return this;
        }

        public Builder requestBody(List<String> requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder elementsPutAll(Map<String, String> elements) {
            if(this.elements == null){
                this.elements = new HashMap<>();
            }
            this.elements.putAll(elements);
            return this;
        }

        public Builder cookie(HttpCookie cookie) {
            this.cookie = cookie;
            return this;
        }

        public Builder session(HttpSession session) {
            this.session = session;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
