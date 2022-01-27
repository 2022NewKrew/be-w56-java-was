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

public class HttpRequestParser {

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        StartLine startLine = parseStartLine(br);
        HttpHeaders httpHeaders = parseHttpHeaders(br);

        int contentLength = Integer.valueOf(httpHeaders.getHeader(HttpHeaders.CONTENT_LENGTH).orElse("0"));
        HttpBody httpBody = parseHttpBody(br, contentLength, StandardCharsets.UTF_8);

        RequestParams parameters = HttpRequestParamsParser.parse(startLine, httpHeaders, httpBody);
        return HttpRequest.of(startLine, httpHeaders, httpBody, parameters);
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
