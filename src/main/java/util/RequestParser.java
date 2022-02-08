package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Map<String, String> requestMap = new HashMap<>();
    BufferedReader br;

    public RequestParser (BufferedReader br) throws IOException {
        this.br = br;
        this.initRequestMap();
    }

    private void initRequestMap () throws IOException {
        // request header first line
        String[] element = br.readLine().split(" ");
        requestMap.put("method", element[0]);
        requestMap.put("url", element[1]);
        requestMap.put("httpVersion", element[2]);

        String line;
        while (!(line = br.readLine()).equals("")) {
            log.debug("other: {}", line);
            HttpRequestUtils.Pair keyValue = HttpRequestUtils.parseHeader(line);
            requestMap.put(keyValue.getKey(), keyValue.getValue());
        }

        int bodyLength = Integer.parseInt(requestMap.getOrDefault("Content-Length", "0"));
        char[] body = new char[bodyLength];
        br.read(body, 0, bodyLength);
        requestMap.put("body", new String(body));
    }

    public Map<String, String> getRequestMap () {
        return this.requestMap;
    }

}
