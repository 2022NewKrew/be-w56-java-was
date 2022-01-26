package http.parser;

import http.*;
import util.HttpRequestUtils;
import util.Pair;
import util.StringUtils;
import webserver.exception.BadRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestParser {
    private static final String REQUEST_HEADER_SEPARATOR = ":";
    private static final String REQUEST_LINE_SEPARATOR = "\\s+";

    public HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        List<String> headerLines = readBufferedReaderRequestHeader(br);
        RequestLine requestInfo = parseRequestInfo(headerLines.remove(0));
        HttpHeaders headers = parseHeaders(headerLines);
        String requestBody = null;
        if(hasBody(headers)) {
            requestBody = readBufferedReaderRequestBody(br, headers);
        }
        return new HttpRequest(requestInfo.httpMethod, requestInfo.requestUri, requestInfo.httpRequestParams, headers, requestBody);
    }

    private List<String> readBufferedReaderRequestHeader(BufferedReader br) throws IOException {
        List<String> lines = new ArrayList<>();
        String line = null;
        while(((line = br.readLine()) != null) && !line.equals("")) {
            lines.add(line);
        }
        return lines;
    }

    private String readBufferedReaderRequestBody(BufferedReader br, HttpHeaders headers) throws IOException {
        int contentLength = Integer.parseInt(headers.getHeaderByName("Content-Length"));
        char[] bytes = new char[contentLength];
        int readBytes = br.read(bytes, 0, contentLength);
        return String.valueOf(bytes);
    }

    private boolean hasBody(HttpHeaders headers) {
        return headers.getHeaderByName("Content-Type") != null;
    }

    private RequestLine parseRequestInfo(String requestLineString) {
        RequestLine requestLine = null;
        try {
            String[] splitRequestLine = requestLineString.split(REQUEST_LINE_SEPARATOR);
            HttpMethod method = HttpMethod.valueOf(splitRequestLine[0]);
            URI uri = new URI(splitRequestLine[1]);
            HttpRequestParams requestParams = parseQueryString(uri.getQuery());
            requestLine = new RequestLine(method, uri, requestParams);
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getClass().getName(), e);
        }
        return requestLine;
    }

    private HttpRequestParams parseQueryString(String queryString) {
        Map<String, String> requestParams = new HashMap<>();
        if(StringUtils.isEmpty(queryString)) {
            return new HttpRequestParams(requestParams);
        }
        requestParams.putAll(HttpRequestUtils.parseQueryString(queryString));
        return new HttpRequestParams(requestParams);
    }

    private HttpHeaders parseHeaders(List<String> headers) {
        Map<String, String> headerMap = new HashMap<>();
        for(String header : headers) {
            Pair<String, String> headerPair = splitHeader(header);
            headerMap.put(headerPair.getFirst(), headerPair.getSecond());
        }
        return new HttpHeaders(headerMap);
    }

    private Pair<String, String> splitHeader(String header) {
        String[] splitHeader = header.split(REQUEST_HEADER_SEPARATOR);
        return new Pair<>(StringUtils.trim(splitHeader[0]), StringUtils.trim(splitHeader[1]));
    }

    private static class RequestLine {
        HttpMethod httpMethod;
        HttpRequestParams httpRequestParams;
        URI requestUri;

        private RequestLine(HttpMethod httpMethod, URI requestUri, HttpRequestParams httpRequestParams) {
            this.httpMethod = httpMethod;
            this.requestUri = requestUri;
            this.httpRequestParams = httpRequestParams;
        }
    }
}
