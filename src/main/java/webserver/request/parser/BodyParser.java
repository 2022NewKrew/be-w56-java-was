package webserver.request.parser;

import util.HttpRequestUtils;
import util.IOUtils;
import webserver.http.request.HttpBody;
import webserver.http.request.HttpHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BodyParser {

    private BodyParser() {

    }

    public static HttpBody parse(BufferedReader br, HttpHeader header) throws IOException {
        Map<String, String> bodyMap = new HashMap<>();
        if (header.getHeaderAttribute("Content-Length") != null) {
            String body = IOUtils.readData(br, Integer.parseInt(header.getHeaderAttribute("Content-Length")));
            bodyMap = HttpRequestUtils.parseBody(body);
        }
        return new HttpBody(bodyMap);
    }
}
