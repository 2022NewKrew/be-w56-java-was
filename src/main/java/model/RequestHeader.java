package model;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private final String method;
    private final String uri;
    private final String protocol;
    private Map<String, String> params = new HashMap<>();

    public RequestHeader(BufferedReader requestHeaderInfo) throws IOException {
        String[] headerToken = requestHeaderInfo.readLine().split(" ");
        String[] uriToken = headerToken[1].split("\\?");
        this.method = headerToken[0];
        this.protocol = headerToken[2];
        this.uri = uriToken[0];
        if (uriToken.length > 1) {
            System.out.println(uriToken[1]);
            this.params = HttpRequestUtils.parseQueryString(uriToken[1]);
        }
    }

    public Map<String, String> getParams() {
        return params;
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
}
