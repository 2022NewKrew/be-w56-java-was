package util;

import httpmodel.HttpCookie;
import httpmodel.HttpMethod;
import httpmodel.HttpRequest;
import httpmodel.HttpSession;
import httpmodel.HttpSessions;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

public class HttpRequestConverter {

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestConverter.class);

    private HttpRequestConverter() {
    }

    public static HttpRequest createdRequest(BufferedReader bufferedReader) {
        try {
            String line = bufferedReader.readLine();

            String[] requestUri = line.split(" ");
            HttpMethod httpMethod = HttpMethod.valueOf(requestUri[0]);
            String uri = requestUri[1];
            String httpVersion = requestUri[2];
            Map<String, String> header = getHeader(bufferedReader);
            Map<String, String> body = getBody(bufferedReader, header, httpMethod);
            HttpCookie httpCookie = getCookie(header.get("Cookie"));
            HttpSession httpSession = getHttpSession(httpCookie);

            return new HttpRequest.Builder()
                .method(httpMethod)
                .uri(uri)
                .httpVersion(httpVersion)
                .header(header)
                .body(body)
                .cookie(httpCookie)
                .httpSession(httpSession)
                .build();
        } catch (Exception e) {
            logger.error("Request Error : {}", e.getMessage());
            throw new IllegalArgumentException("[ERROR] 잘못된 Request 입니다.");
        }
    }

    private static Map<String, String> getHeader(BufferedReader bufferedReader) throws IOException {
        Map<String, String> header = new HashMap<>();

        String line = bufferedReader.readLine();
        while (!"".equals(line) && Objects.nonNull(line)) {
            Pair pair = HttpRequestUtils.parseHeader(line);
            header.put(pair.getKey(), pair.getValue());
            line = bufferedReader.readLine();
        }

        return header;
    }

    private static Map<String, String> getBody(BufferedReader bufferedReader,
        Map<String, String> header,
        HttpMethod httpMethod) throws IOException {

        Map<String, String> requestBody = new HashMap<>();

        if (httpMethod.isBody() && header.containsKey(CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(header.get(CONTENT_LENGTH));
            char[] buffer = new char[contentLength];
            int read = bufferedReader.read(buffer, 0, contentLength);
            if (read == -1) {
                return requestBody;
            }
            String line = new String(buffer);
            String[] bodyDates = line.split("&");
            for (String body : bodyDates) {
                String[] keyValue = body.split("=");
                requestBody.put(keyValue[0], keyValue[1]);
            }
        }
        return requestBody;
    }

    private static HttpCookie getCookie(String cookie) {
        return new HttpCookie(HttpRequestUtils.parseCookies(cookie));
    }

    private static HttpSession getHttpSession(HttpCookie httpCookie) {
        String sessionId = httpCookie.getCookie("JSESSIONID");
        if (Objects.isNull(sessionId)) {
            sessionId = UUID.randomUUID().toString();
        }
        HttpSession session = HttpSessions.getSession(sessionId);
        if (Objects.nonNull(session)) {
            return session;
        }
        return new HttpSession(sessionId);
    }
}
