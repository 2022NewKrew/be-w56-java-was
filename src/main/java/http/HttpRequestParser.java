package http;

import util.HttpRequestUtils;
import util.Pair;
import util.StringUtils;
import webserver.exception.BadRequestException;
import webserver.exception.InternalServerErrorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private static final String REQUEST_HEADER_SEPARATOR = ":";
    private static final String REQUEST_LINE_SEPARATOR = "\\s+";

    public HttpRequest parse(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestInfo requestInfo = parseRequestInfo(br);
        HttpHeaders httpHeaders = parseHeaders(br);
        return new HttpRequest(requestInfo.httpMethod, requestInfo.requestUri, requestInfo.httpRequestParams, httpHeaders, null);
    }

    private RequestInfo parseRequestInfo(BufferedReader br) {
        RequestInfo requestInfo = null;
        try {
            String requestLine = br.readLine();
            String[] splitRequestLine = requestLine.split(REQUEST_LINE_SEPARATOR);
            HttpMethod method = HttpMethod.valueOf(splitRequestLine[0]);
            URI uri = new URI(splitRequestLine[1]);
            HttpRequestParams requestParams = parseQueryString(uri.getQuery());
            requestInfo = new RequestInfo(method, uri, requestParams);
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getClass().getName(), e);
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getClass().getName(), e);
        }
        return requestInfo;
    }

    private HttpRequestParams parseQueryString(String queryString) {
        Map<String, String> requestParams = new HashMap<>();
        if(StringUtils.isEmpty(queryString)) {
            return new HttpRequestParams(requestParams);
        }
        requestParams.putAll(HttpRequestUtils.parseQueryString(queryString));
        return new HttpRequestParams(requestParams);
    }

    private HttpHeaders parseHeaders(BufferedReader br) {
        Map<String, String> map = new HashMap<>();
        try {
            String header = br.readLine();
            while(validHeaderValue(header)) {
                Pair<String, String> headerPair = splitHeader(header);
                map.put(headerPair.getFirst(), headerPair.getSecond());
                header = br.readLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpHeaders(map);
    }

    private Pair<String, String> splitHeader(String header) {
        String[] splitHeader = header.split(REQUEST_HEADER_SEPARATOR);
        return new Pair<>(StringUtils.trim(splitHeader[0]), StringUtils.trim(splitHeader[1]));
    }

    private boolean validHeaderValue(String header) {
        return !StringUtils.isEmpty(header);
    }

    private static class RequestInfo {
        HttpMethod httpMethod;
        HttpRequestParams httpRequestParams;
        URI requestUri;

        private RequestInfo(HttpMethod httpMethod, URI requestUri, HttpRequestParams httpRequestParams) {
            this.httpMethod = httpMethod;
            this.requestUri = requestUri;
            this.httpRequestParams = httpRequestParams;
        }
    }

}
