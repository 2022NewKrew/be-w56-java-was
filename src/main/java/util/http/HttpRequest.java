package util.http;

import util.URL;

import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final URL url;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final Map<String, String> body;

    public HttpRequest(String line) {
        String[] words = line.split(" ");
        this.method = HttpMethod.valueOf(words[0]);
        this.url = new URL(words[1]);
        this.httpVersion = words[2];
        this.headers = null;
        this.body = null;
    }


    public HttpRequest(HttpMethod method, URL url, String httpVersion, Map<String, String> headers) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = null;
    }

    public HttpRequest(HttpMethod method, URL url, String httpVersion, Map<String, String> headers,
                       Map<String, String> body) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
    }


    public HttpMethod method() {
        return this.method;
    }

    public String url() {
        return url.url();
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> params() {
        return url.params();
    }

    public Map<String, String> body(){
        return this.body;
    }

    public String getHeader(String key){
        return headers.get(key);
    }
}
