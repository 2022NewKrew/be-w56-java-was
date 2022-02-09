package webserver.request.parser;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeaderLineParser {

    private HeaderLineParser() {

    }

    public static Map<String, String> parse(BufferedReader br) throws IOException {
        String httpRequestLine = br.readLine();
        Map<String, String> headerMap = new HashMap<>();
        for (HttpRequestUtils.Pair parsedRequestLine : HttpRequestUtils.parseRequestLine(httpRequestLine)) {
            headerMap.put(parsedRequestLine.getKey(), parsedRequestLine.getValue());
        }
        return headerMap;
    }
}
