package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import lombok.Getter;

import webserver.util.IOUtils;

@Getter
public class HttpRequestBody {
    private Map<String, String> body;

    public HttpRequestBody(BufferedReader br, int contentLength) throws IOException {
        String bodyContent = IOUtils.readData(br, contentLength);
        body = new HashMap<>();

        StringTokenizer st = new StringTokenizer(bodyContent, "&");

        while (st.hasMoreTokens()) {
            StringTokenizer innerSt = new StringTokenizer(st.nextToken(), "=");
            body.put(innerSt.nextToken(), URLDecoder.decode(innerSt.nextToken(), "UTF-8"));
        }
    }
}
