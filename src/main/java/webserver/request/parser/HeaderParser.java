package webserver.request.parser;

import util.HttpRequestUtils;
import webserver.http.request.HttpHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeaderParser {

    private HeaderParser() {

    }

    public static HttpHeader parse(BufferedReader br) throws IOException {
        Map<String, String> headerMap = headerLineParse(br);
        String headerLine;
        while (!(headerLine = br.readLine()).equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
            headerMap.put(pair.getKey(), pair.getValue());
        }
        Map<String, String> cookieMap = cookieParse(headerMap.get("Cookie"));
        return new HttpHeader(headerMap, cookieMap);
    }

    private static Map<String, String> headerLineParse(BufferedReader br) throws IOException {
        String httpRequestLine = br.readLine();
        Map<String, String> headerMap = new HashMap<>();
        for (HttpRequestUtils.Pair parsedRequestLine : HttpRequestUtils.parseRequestLine(httpRequestLine)) {
            headerMap.put(parsedRequestLine.getKey(), parsedRequestLine.getValue());
        }
        return headerMap;
    }

    private static Map<String, String> cookieParse(String cookieLine) {
        return HttpRequestUtils.parseCookies(cookieLine);
    }
}
