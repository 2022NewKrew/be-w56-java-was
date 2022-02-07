package webserver.request;

import util.UrlQueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class HttpRequest {
    private final HttpRequestLine httpRequestLine;
    private final HttpRequestHeader httpRequestHeader;
    private final HttpRequestBody httpRequestBody;

    public HttpRequest(HttpRequestLine httpRequestLine, HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody){
        this.httpRequestLine = httpRequestLine;
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public static HttpRequest makeHttpRequest(BufferedReader br) throws IOException {
        String firstLine = br.readLine();

        HttpRequestLine httpRequestLine = HttpRequestLine.makeHttpRequestLine(firstLine);

        List<String> headerList = new ArrayList<>();
        String line = br.readLine();
        while(line != null && !"".equals(line)){
            headerList.add(line);
            line = br.readLine();
        }

        HttpRequestHeader httpRequestHeader = HttpRequestHeader.makeHttpRequestHeader(headerList);

        HttpRequestBody httpRequestBody;
        int len = 0;
        if(!"GET".equals(httpRequestLine.getMethod())) {
            len = Integer.parseInt(httpRequestHeader.getHeaderMap().get("Content-Length"));

            char[] a = new char[len];

            br.read(a, 0, a.length);

            httpRequestBody = HttpRequestBody.makeHttpRequestBody(a);
        }else{
            httpRequestBody = HttpRequestBody.makeHttpRequestBody(httpRequestLine.getRequestUrl());
        }

        return new HttpRequest(httpRequestLine, httpRequestHeader, httpRequestBody);
    }

    public String getMethod() {
        return httpRequestLine.getMethod();
    }

    public String getUrl() {
        return httpRequestLine.getUrl();
    }

    public Map<String, String> getBody() {
        return httpRequestBody.getBodyMap();
    }

    public HttpRequestHeader getHeader() {
        return httpRequestHeader;
    }
}
