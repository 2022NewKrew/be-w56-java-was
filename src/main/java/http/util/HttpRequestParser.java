package http.util;

import http.*;
import http.request.HttpRequest;
import http.request.RequestParams;
import http.request.StartLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestParser {

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        StartLine startLine = parseStartLine(br);
        HttpHeaders httpHeaders = parseHttpHeaders(br);

        Integer contentLength = httpHeaders.getContentLength();
        HttpBody httpBody = parseHttpBody(br, contentLength, StandardCharsets.UTF_8);

        RequestParams parameters = HttpRequestParamsParser.parse(startLine, httpHeaders, httpBody);
        return HttpRequest.of(startLine, httpHeaders, httpBody, parameters);
    }

    private static StartLine parseStartLine(BufferedReader br) throws IOException {
        String[] elements = br.readLine().split(" ");
        return StartLine.of(HttpMethod.getHttpMethod(elements[0]), Url.of(elements[1]), elements[2]);
    }

    private static HttpHeaders parseHttpHeaders(BufferedReader br) throws IOException {
        MultiValueMap<String, String> headerMap = new MultiValueMap<>();
        String line = null;
        while((line = br.readLine()) != null && !("".equals(line))) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            List<String> values = Arrays.asList(pair.getValue().split(","));
            values = values.stream().map(value -> value.trim()).collect(Collectors.toList());
            headerMap.addAll(pair.getKey(), values);
        }
        return HttpHeaders.of(headerMap);
    }

    private static HttpBody parseHttpBody(BufferedReader br, Integer contentLength, Charset charset) throws IOException {
        return HttpBody.of(IOUtils.readData(br, contentLength).getBytes(charset));
    }

}
