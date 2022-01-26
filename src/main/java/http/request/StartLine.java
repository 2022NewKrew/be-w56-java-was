package http.request;

import http.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;

public class StartLine {

    private HttpMethod method;

    private Url url;

    private String httpVersion;

    public StartLine(HttpMethod method, Url url, String httpVersion) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
    }

    public static StartLine create(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] elements = line.split(" ");
        return new StartLine(HttpMethod.getHttpMethod(elements[0]), Url.of(elements[1]), elements[2]);
    }

    public HttpMethod getHttpMethod() {
        return method;
    }

    public Url getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
