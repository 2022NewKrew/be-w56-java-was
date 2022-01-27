package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MyHttpRequest {
    private String method = "";
    private String uri = "";
    private String protocol = "";
    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MyHttpRequest.class);

    public MyHttpRequest() {}

    public MyHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String request = br.readLine();
        logger.debug("request : {}", request);
        setRequest(request);

        br.lines().takeWhile(line -> !line.equals(""))
                .forEach(this::setHeader);
    }

    public void setRequest(String request) {
        String[] tokens = request.split(" ");

        this.method = tokens[0];
        setUri(tokens[1]);
        this.protocol = tokens[2];
    }

    public void setUri(String request) {
        if (request.contains("?")) {
            String[] tokens = request.split("\\?");
            this.uri = tokens[0];
            Stream.of(tokens[1].split("&"))
                    .forEach(this::setParameters);
        } else {
            this.uri = request;
        }
    }

    private void setParameters(String parameter) {
        String[] tokens = parameter.split("=");
        this.parameters.put(tokens[0], tokens[1]);
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

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }
}
