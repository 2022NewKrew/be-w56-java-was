package http;

import http.*;
import http.exception.BadHttpFormatException;
import util.Pair;
import util.StringUtils;
import webserver.exception.BadRequestException;
import webserver.processor.handler.controller.Request;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestParser {
    private static final String REQUEST_HEADER_SEPARATOR = ":";
    private static final String REQUEST_LINE_SEPARATOR = "\\s+";

    private static final char CR = '\r';
    private static final char LF = '\n';
    private static final int EOF = -1;

    public HttpRequest parse(InputStream in) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(in);
        List<String> headerLines = readRequestHeaderLines(bis);
        checkHeaderEmpty(headerLines);
        RequestLine requestInfo = parseRequestInfo(headerLines.remove(0));
        HttpHeaders headers = parseHeaders(headerLines);
        byte[] requestBody = readRequestBody(bis, headers);
        return new HttpRequest(requestInfo.httpMethod, requestInfo.requestUri, headers, requestBody);
    }

    private void checkHeaderEmpty(List<String> headerLines) {
        if(headerLines.size() <= 0) {
            throw new BadHttpFormatException("RequestLine and Headers are Empty");
        }
    }

    private String readLine(BufferedInputStream bis) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int readByte = Integer.MIN_VALUE;
        int buffer = Integer.MIN_VALUE;
        while (true) {
            readByte = bis.read();
            if (readByte == EOF) {
                break;
            }
            if (readByte == LF && buffer == CR) {
                break;
            } else if (readByte != LF && buffer == CR) {
                bos.write(buffer);
            }
            if (readByte == CR) {
                buffer = readByte;
                continue;
            }
            bos.write(readByte);
        }
        return bos.toString(StandardCharsets.US_ASCII);
    }

    private List<String> readRequestHeaderLines(BufferedInputStream bis) throws IOException {
        List<String> headerLines = new ArrayList<>();
        String line = "";
        while (!(line = readLine(bis)).equals("")) {
            headerLines.add(line);
        }
        return headerLines;
    }

    private byte[] readRequestBody(BufferedInputStream inputStream, HttpHeaders httpHeaders) throws IOException {
        if (!hasBody(httpHeaders)) {
            return null;
        }
        int contentLength = Integer.parseInt(httpHeaders.getHeaderByName("Content-Length"));
        byte[] readBytes = new byte[contentLength];
        inputStream.read(readBytes, 0, contentLength);
        return readBytes;
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
            requestLine = new RequestLine(method, uri);
        } catch (URISyntaxException e) {
            throw new BadRequestException(e.getClass().getName(), e);
        }
        return requestLine;
    }

    private HttpHeaders parseHeaders(List<String> headers) {
        Map<String, String> headerMap = new HashMap<>();
        for (String header : headers) {
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
        URI requestUri;

        private RequestLine(HttpMethod httpMethod, URI requestUri) {
            this.httpMethod = httpMethod;
            this.requestUri = requestUri;
        }
    }
}
