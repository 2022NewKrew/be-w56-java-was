package webserver.requesthandler;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import webserver.common.util.HttpRequestUtils;
import webserver.common.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Getter
public class HttpRequest {
    private final HttpRequestStartLine startLine;
    private final Map<String, String> headers;
    private final Map<String, String> requestBody;

    public static HttpRequest doRequest(BufferedReader br) throws IOException {
        HttpRequestStartLine startLine = inputStartLine(br.readLine());
        Map<String, String> headers = HttpRequest.inputHeader(br);
        Map<String, String> requestBody = Collections.emptyMap();
        if (headers.containsKey("Content-Length")) {
            requestBody = HttpRequest.inputBody(IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length"))));
        }

        return new HttpRequest(startLine, headers, requestBody);
    }

    public static HttpRequestStartLine inputStartLine(String startLineString) throws IOException {
        log.debug("[HTTP Request]");
        log.debug("[Start line] " + startLineString);
        startLineString = URLDecoder.decode(startLineString, StandardCharsets.UTF_8);

        HttpRequestStartLine startLine = HttpRequestUtils.parseStartLine(startLineString);
        assert startLine != null;
        return startLine;
    }

    public static Map<String, String> inputHeader(BufferedReader br) throws IOException {
        Map<String, String> parsedHeaders = new HashMap<>();
        String line = null;
        log.debug("[Headers]");
        while (!"".equals(line)) {
            line = br.readLine();
            log.debug(line);
            if (Strings.isNullOrEmpty(line)) {
                break;
            }
            HttpRequestUtils.Pair parsed = HttpRequestUtils.parseHeader(line);
            parsedHeaders.put(parsed.getKey(), parsed.getValue());
        }
        return parsedHeaders;
    }

    public static Map<String, String> inputBody(String requestBodyString) {
        log.debug("[Request body] " + requestBodyString);
        requestBodyString = URLDecoder.decode(requestBodyString, StandardCharsets.UTF_8);
        return HttpRequestUtils.parseQueryString(requestBodyString);
    }
}
