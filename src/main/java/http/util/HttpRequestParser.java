package http.util;

import http.HttpBody;
import http.HttpHeaders;
import http.HttpMethod;
import http.Url;
import http.request.HttpRequest;
import http.request.RequestParams;
import http.request.StartLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static http.util.HttpRequestUtils.parseQueryString;

public class HttpRequestParser {

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        StartLine startLine = parseStartLine(br);
        HttpHeaders httpHeaders = parseHttpHeaders(br);

        int contentLength = Integer.valueOf(httpHeaders.getHeader(HttpHeaders.CONTENT_LENGTH).orElse("0"));
        HttpBody httpBody = parseHttpBody(br, contentLength, StandardCharsets.UTF_8);

        Map<String, Object> parameterMap = new HashMap<>();
        Map<String, String> queryMap = parseQueryString(startLine.getUrl().getQueryString());
        for(Map.Entry<String, String> query : queryMap.entrySet()) {
            parameterMap.put(query.getKey(), query.getValue());
        }

        if(httpHeaders.getHeader(HttpHeaders.CONTENT_TYPE).orElseGet(()->"").equals("application/x-www-form-urlencoded")
                && startLine.getHttpMethod().equals(HttpMethod.POST)) {
            Map<String, String> queryMap2 = parseQueryString(httpBody.toString(StandardCharsets.UTF_8));
            for(Map.Entry<String, String> query : queryMap2.entrySet()) {
                parameterMap.put(query.getKey(), query.getValue());
            }
        }

        return HttpRequest.of(startLine, httpHeaders, httpBody, RequestParams.of(parameterMap));
    }

    private static StartLine parseStartLine(BufferedReader br) throws IOException {
        String[] elements = br.readLine().split(" ");
        return StartLine.of(HttpMethod.getHttpMethod(elements[0]), Url.of(elements[1]), elements[2]);
    }

    private static HttpHeaders parseHttpHeaders(BufferedReader br) throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        String line = null;
        while((line = br.readLine()) != null && !("".equals(line))) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headerMap.put(pair.getKey(), pair.getValue());
        }
        return HttpHeaders.of(headerMap);
    }

    private static HttpBody parseHttpBody(BufferedReader br, int contentLength, Charset charset) throws IOException {
        return HttpBody.of(IOUtils.readData(br, contentLength).getBytes(charset));
    }

}
