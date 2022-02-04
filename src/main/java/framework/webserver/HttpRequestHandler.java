package framework.webserver;

import framework.util.Cookies;
import framework.util.HttpSession;
import framework.util.RequestAttributes;
import framework.util.RequestHeaders;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static framework.util.Constants.DEFAULT_HTTP_VERSION;
import static framework.util.Constants.SESSION_ID_KEY;

/**
 * Client의 요청 정보를 담은 클래스
 */
@Getter
public class HttpRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandler.class);

    private String requestMethod;
    private String uri;
    private String httpVersion = DEFAULT_HTTP_VERSION;
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
        String[] splited = requestLine.split(" ");

        requestMethod = splited[0];
        parseUri(splited[1]);
        httpVersion = splited[2];

        LOGGER.debug("Method: {}, URI: {}", requestMethod, uri);
    }

    private void parseUri(String originUri) {
        uri = URLDecoder.decode(originUri, StandardCharsets.UTF_8);

        // Welcome Page 설정
        if ("/".equals(uri) || "/index".equals(uri)) {
            uri = "/index.html";
        }

        // 만약 URI의 첫 글자가 '/'가 아니라면 붙여줌
        if (uri.charAt(0) != '/') {
            StringBuilder sb = new StringBuilder("/");
            uri = sb.append(uri).toString();
        }

        // GET 요청을 통해 파라미터를 전달했을 경우 해당 파라미터들을 파싱하여 저장
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

    public Object getRequestHeader(String key) {
        return requestHeaders.getRequestHeader(key);
    }

    public Cookies getCookies() {
        if (!requestHeaders.contains("Cookie")) {
            return new Cookies();
        }

        return (Cookies) requestHeaders.getRequestHeader("Cookie");
    }

    public HttpSession getSession() {
        Cookies cookies = getCookies();

        if (!cookies.contains(SESSION_ID_KEY)) {
            cookies.setCookie(SESSION_ID_KEY, HttpSessionHandler.makeSession());
        }

        String sessionId = cookies.getCookie(SESSION_ID_KEY);

        if (!HttpSessionHandler.contains(sessionId)) {
            HttpSessionHandler.makeSessionWithId(sessionId);
        }

        return HttpSessionHandler.getSession(sessionId);
    }

    public String getAttribute(String key) {
        return requestAttributes.getAttribute(key);
    }
}
