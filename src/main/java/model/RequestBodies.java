package model;

import com.google.common.collect.Maps;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestBodies {
    private static final String CONTENT_LENGTH = "Content-Length";

    private final Map<String, String> requestBodies;

    private RequestBodies(Map<String, String> requestBodies) {
        this.requestBodies = new HashMap<>(requestBodies);
    }

    public static RequestBodies of(BufferedReader br, RequestHeaders requestHeaders) throws IOException {
        if (!requestHeaders.isContainKey(CONTENT_LENGTH)) {
             return new RequestBodies(Maps.newHashMap());
        }

        final int contentLength = Integer.parseInt(requestHeaders.getHeader(CONTENT_LENGTH));
        return new RequestBodies(HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength)));
    }

    public String getRequestBody(String key) {
        return requestBodies.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String key : requestBodies.keySet()) {
            sb.append(key).append(" : ").append(requestBodies.get(key)).append("\n");
        }

        return "RequestBody{" +
                "requestBodies=\n" + sb +
                '}';
    }
}
