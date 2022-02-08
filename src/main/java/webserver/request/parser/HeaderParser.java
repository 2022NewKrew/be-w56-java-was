package webserver.request.parser;

import util.HttpRequestUtils;
import webserver.http.request.HttpHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class HeaderParser {

    private HeaderParser() {

    }

    public static HttpHeader parse(BufferedReader br) throws IOException {
        Map<String, String> headerMap = HeaderLineParser.parse(br);
        String headerLine;
        while(!(headerLine = br.readLine()).equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
            headerMap.put(pair.getKey(), pair.getValue());
        }
        Map<String, String> cookieMap = cookieParse(headerMap.get("Cookie"));
        return new HttpHeader(headerMap, cookieMap);
    }

    private static Map<String, String> cookieParse(String cookieLine) {
        return HttpRequestUtils.parseCookies(cookieLine);
    }
}
