package webserver.request.parser;

import util.HttpRequestUtils;
import webserver.http.RequestLine;
import webserver.http.request.HttpHeader;
import webserver.http.request.HttpUrlQuery;

import java.util.HashMap;
import java.util.Map;

public class QueryParser {

    private QueryParser() {

    }

    public static HttpUrlQuery parse(HttpHeader httpHeader) {
        Map<String, String> queryMap = new HashMap<>();
        String[] parsedPath = HttpRequestUtils.parseGetRequest(httpHeader.getHeaderAttribute(RequestLine.PATH.name()));
        if (parsedPath.length == 2) {
            queryMap = HttpRequestUtils.parseQueryString(parsedPath[1]);
            httpHeader.setPath(parsedPath[0]);
        }
        return new HttpUrlQuery(queryMap);
    }
}
