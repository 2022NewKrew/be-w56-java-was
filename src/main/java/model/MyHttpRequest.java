package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import webserver.enums.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MyHttpRequest {
    private HttpMethod method;
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
                .peek(h -> logger.debug("        : {}", h))
                .forEach(this::setHeader);

        if(method.equals(HttpMethod.POST)) {
            String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            Stream.of(body.split("&"))
                    .forEach(this::setParameters);
        }
    }

    public void setRequest(String request) throws IOException {
        String[] tokens = request.split(" ");

        if (tokens.length != 3) {
            throw new IOException("http request 포맷이 잘못 되었습니다.");
        }

        this.method = HttpMethod.valueOf(tokens[0]);
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

    public HttpMethod getMethod() {
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
