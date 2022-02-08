package webserver.request.parser;

import util.HttpRequestUtils;
import webserver.http.request.HttpHeader;
import webserver.http.request.HttpUrlQuery;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class QueryParser {

    private QueryParser() {

    }

    public static HttpUrlQuery parse(BufferedReader br, HttpHeader httpHeader) {
        Map<String, String> queryMap = new HashMap<>();
        String[] parsedPath = HttpRequestUtils.parseGetRequest(httpHeader.getHeaderAttribute("PATH"));
        if(parsedPath.length == 2) {
            queryMap = HttpRequestUtils.parseQueryString(parsedPath[1]);
            httpHeader.setPath(parsedPath[0]);
        }
        return new HttpUrlQuery(queryMap);
    }
}
