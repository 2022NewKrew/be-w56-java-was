package util;

import model.RequestData;
import model.User;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpHeaderUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpHeaderUtils.class);

    public static String getHttpRequestUrl(String request) throws IllegalArgumentException {
        String[] token = request.split(" ");
        if(token.length < 3) {
            throw new IllegalArgumentException("request = " + request);
        }
        return token[1];
    }

    public static Optional<String> getQuery(String urlWithQuery) {
        String[] token = urlWithQuery.split("\\?");
        if(token.length < 2) {
            return Optional.empty();
        }
        return Optional.of(urlWithQuery.split("\\?")[1]);
    }

    public static String getUrl(String urlWithQuery) {
        return urlWithQuery.split("\\?")[0];
    }

    public static RequestData parseRequestLine(String requestLine) {
        String[] token = requestLine.split(" ");
        String method = token[0];
        String url = token[1];
        String[] urlToken = url.split("\\?");

        String urlPath = urlToken[0];
        String urlQuery = "";
        if(urlToken.length >= 2) {
            urlQuery = urlToken[1];
        }
        String httpVersion = token[2];
        return new RequestData(method, urlPath, urlQuery, httpVersion);
    }

    public static User getUserInfoFromUrl(String query) {
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(query);
        String userId = URLDecoder.decode(userInfo.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(userInfo.get("password"), StandardCharsets.UTF_8);
        String name = URLDecoder.decode(userInfo.get("name"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(userInfo.get("email"), StandardCharsets.UTF_8);

        return new User(userId, password, name, email);
    }

    public static String getContentTypeFromUrl(String url) {
        String extension = FilenameUtils.getExtension(url);
        switch (extension) {
            case "html": return "text/html";
            case "css": return "text/css";
            case "js": return "text/javascript";
            case "ico": return "image/x-icon";
            default: return "text/html";
        }
    }

    public static Optional<User> parseUserInfo(String requestBody) {
        log.info("requestBody = {}", requestBody);
        if (requestBody.length() > 0) {
            User user = HttpHeaderUtils.getUserInfoFromUrl(requestBody);
            log.info("user = {}", user);
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
