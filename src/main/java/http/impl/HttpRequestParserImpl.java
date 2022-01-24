package http.impl;

import http.HttpHeaders;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpRequestParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

class HttpRequestParserImpl implements HttpRequestParser {

    @Override
    public HttpRequest parse(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestInfo requestInfo = parseRequestInfo(br);
        HttpHeaders httpHeaders = parseHeaders(br);
        return new HttpRequestImpl(requestInfo.httpMethod, requestInfo.uri, httpHeaders);
    }

    private RequestInfo parseRequestInfo(BufferedReader br) {
        RequestInfo requestInfo = null;
        try {
            String requestLine = br.readLine();
            String[] splitRequestLine = requestLine.split(" ");
            HttpMethod method = HttpMethod.valueOf(splitRequestLine[0]);
            URI uri = new URI(splitRequestLine[1]);
            requestInfo = new RequestInfo(method, uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return requestInfo;
    }

    private HttpHeaders parseHeaders(BufferedReader br) {
        Map<String, String> map = new HashMap<>();
        try {
            String header = br.readLine();
            while(validHeaderValue(header)) {
                Map.Entry<String,String> splitHeader = splitHeader(header);
                map.put(splitHeader.getKey(), splitHeader.getValue());
                header = br.readLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpHeadersImpl(map);
    }

    private Map.Entry<String, String> splitHeader(String header) {
        String[] splitHeader = header.split(":");
        return Map.entry(trim(splitHeader[0]), trim(splitHeader[1]));
    }

    private boolean validHeaderValue(String header) {
        return header != null && !header.isEmpty();
    }

    private String trim(String value) {
        return value.trim();
    }

    private static class RequestInfo {
        HttpMethod httpMethod;
        URI uri;

        private RequestInfo(HttpMethod httpMethod, URI uri) {
            this.httpMethod = httpMethod;
            this.uri = uri;
        }
    }
}
