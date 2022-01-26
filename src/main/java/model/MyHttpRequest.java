package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MyHttpRequest {
    private String method = "";
    private String uri = "";
    private String protocol = "";
    private final Map<String, String> headers = new HashMap<>();

    public MyHttpRequest() {}

    public MyHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String request = br.readLine();

        setRequest(request);

        br.lines().takeWhile(line -> !line.equals(""))
                .forEach(this::setHeader);
    }

    public void setRequest(String request) {
        String[] tokens = request.split(" ");

        this.method = tokens[0];
        this.uri = tokens[1];
        this.protocol = tokens[2];
    }

    public void setHeader(String header) {
        String[] tokens = header.split(": ");
        this.headers.put(tokens[0], tokens[1]);
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAccept() {
        return headers.get("Accept").split(",")[0];
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
