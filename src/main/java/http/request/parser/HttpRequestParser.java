package http.request.parser;

import http.request.HttpRequest;
import http.request.HttpRequestHeader;
import http.request.HttpRequestLine;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestParser {

    public HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        HttpRequestLine httpRequestLine = parseRequestLine(br);
        HttpRequestHeader httpRequestHeader = parseRequestHeaderAfterParsingRequestLine(br);
        String httpRequestBody = httpRequestHeader.getHeader("Content-Length")
                .map(contentLength -> parseRequestBodyAfterParsingRequestHeader(br, Integer.parseInt(contentLength)))
                .orElse("");

        return new HttpRequest(httpRequestLine, httpRequestHeader, httpRequestBody);
    }

    private HttpRequestLine parseRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        if (requestLine == null) {
            throw new IllegalArgumentException("Request Header가 정상적이지 않습니다.");
        }

        return new HttpRequestLine(requestLine);
    }

    private HttpRequestHeader parseRequestHeaderAfterParsingRequestLine(BufferedReader br) throws IOException {
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader();
        String header = br.readLine();
        while (!"".equals(header) && header != null) {
            String headerName = header.substring(0, header.indexOf(":"));
            String headerContent = header.substring(header.indexOf(":") + 2);
            httpRequestHeader.addHeader(headerName, headerContent);
            header = br.readLine();
        }

        return httpRequestHeader;
    }

    private String parseRequestBodyAfterParsingRequestHeader(BufferedReader br, int contentLength)  {
        return IOUtils.readData(br, contentLength);
    }

}
