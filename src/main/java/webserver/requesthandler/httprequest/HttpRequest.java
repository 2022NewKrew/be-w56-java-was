package webserver.requesthandler.httprequest;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import webserver.common.util.HttpUtils;
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
    private final Map<String, String> header;
    private final Map<String, String> body;

    public static HttpRequest doRequest(BufferedReader br) throws IOException {
        log.debug("[HTTP Request]");
        HttpRequestStartLine startLine = inputStartLine(br);
        Map<String, String> headers = HttpRequest.inputHeader(br);
        Map<String, String> body = Collections.emptyMap();
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            body = HttpRequest.inputBody(br, contentLength);
        }

        return new HttpRequest(startLine, headers, body);
    }

    public static HttpRequestStartLine inputStartLine(BufferedReader br) throws IOException {
        String startLineString = br.readLine();
        log.debug("[Start line] " + startLineString);
        startLineString = URLDecoder.decode(startLineString, StandardCharsets.UTF_8);

        HttpRequestStartLine startLine = HttpUtils.parseStartLine(startLineString);
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
            HttpUtils.Pair parsed = HttpUtils.parseHeader(line);
            parsedHeaders.put(parsed.getKey(), parsed.getValue());
        }
        return parsedHeaders;
    }

    public static Map<String, String> inputBody(BufferedReader br, int contentLength) throws IOException {
        String bodyString = IOUtils.readData(br, contentLength);
        log.debug("[Request body] " + bodyString);
        bodyString = URLDecoder.decode(bodyString, StandardCharsets.UTF_8);
        return HttpUtils.parseQueryString(bodyString);
    }
}
