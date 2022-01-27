package framework.webserver;

import lombok.Getter;
import framework.util.RequestAttributes;
import framework.util.RequestHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static framework.util.Constants.DEFAULT_HTTP_VERSION;

@Getter
public class HttpRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandler.class);

    private String requestMethod;
    private String uri;
    private String httpVersion;
    private final RequestHeaders requestHeaders = new RequestHeaders();
    private final RequestAttributes requestAttributes = new RequestAttributes();

    public HttpRequestHandler(BufferedReader br) throws Exception {
        parseRequestLine(br.readLine());
        requestHeaders.parseRequestHeaders(br);

        if (!requestMethod.equals("GET")) {
            parseRequestBody(br);
        }
    }

    private void parseRequestLine(String requestLine) {
        clearInfos();

        String[] splited = requestLine.split(" ");

        requestMethod = splited[0];
        parseUri(splited[1]);
        httpVersion = splited[2];

        LOGGER.debug("Method: {}, URI: {}", requestMethod, uri);
    }

    private void clearInfos() {
        requestMethod = null;
        uri = null;
        httpVersion = DEFAULT_HTTP_VERSION;
        requestHeaders.clear();
        requestAttributes.clear();
    }

    private void parseUri(String originUri) {
        uri = URLDecoder.decode(originUri, StandardCharsets.UTF_8);

        // Welcome Page 설정
        if ("/".equals(uri) || "/index".equals(uri)) {
            uri = "/index.html";
        }

        if (uri.charAt(0) != '/') {
            StringBuilder sb = new StringBuilder("/");
            uri = sb.append(uri).toString();
        }

        if (uri.contains("?")) {
            String[] splited = uri.split("\\?");
            uri = splited[0];
            requestAttributes.parseAttributes(splited[1]);
        }
    }

    private void parseRequestBody(BufferedReader br) throws Exception {
        int contentLength = (int) requestHeaders.getRequestHeader("Content-Length");
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        String attributeStr = URLDecoder.decode(String.copyValueOf(body), StandardCharsets.UTF_8);

        LOGGER.debug("Attributes: {}", attributeStr);
        requestAttributes.parseAttributes(attributeStr);
    }

    public String getAttribute(String key) {
        return requestAttributes.getAttribute(key);
    }
}
