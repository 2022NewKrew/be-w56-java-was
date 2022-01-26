package webserver.common.util;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

import static webserver.common.exception.ExceptionMessage.*;

@Getter
@AllArgsConstructor
public class HttpRequestStartLine {
    private final String method;
    private final String url;
    private final Map<String, String> queryParameters;
    private final String version;

    public static HttpRequestStartLine valueOf(
            String method,
            String url,
            String version,
            Map<String, String> queryParameters
    ) {
        validateMethod(method);
        validateUrl(url);
        url = initializeUrlToIndexPage(url);
        validateHttpVersion(version);

        return new HttpRequestStartLine(method, url, queryParameters, version);
    }

    private static void validateMethod(String method) throws UnsupportedOperationException, NullPointerException {
        if (Strings.isNullOrEmpty(method)) {
            throw new NullPointerException(HTTP_METHOD_NOT_FOUND_EXCEPTION.getMessage());
        }

        if (!(Objects.equals(method, "GET") || Objects.equals(method, "POST"))) {
            throw new UnsupportedOperationException(UNSUPPORTED_HTTP_METHOD_EXCEPTION.getMessage());
        }
    }

    private static void validateUrl(String url) throws NullPointerException {
        if (Strings.isNullOrEmpty(url)) {
            throw new NullPointerException(URL_NOT_FOUND_EXCEPTION.getMessage());
        }
    }

    private static String initializeUrlToIndexPage(String url) {
        return Objects.equals(url, "/") ? "/index.html" : url;
    }

    private static void validateHttpVersion(String version) throws IllegalArgumentException, NullPointerException {
        if (Strings.isNullOrEmpty(version)) {
            throw new NullPointerException(HTTP_VERSION_NOT_FOUND_EXCEPTION.getMessage());
        }

        String[] parsed = version.split("/");
        if (parsed[1] == null ||
                !(Objects.equals(parsed[1], "0.9") ||
                        Objects.equals(parsed[1], "1.0") ||
                        Objects.equals(parsed[1], "1.1") ||
                        Objects.equals(parsed[1], "2.0") ||
                        Objects.equals(parsed[1], "3.0"))
        ) {
            throw new IllegalArgumentException(INVALID_HTTP_VERSION_EXCEPTION.getMessage());
        }
    }

    @Override
    public String toString() {
        return "HttpRequestStartLine [method=" + method + ", url=" + url + ", queryParameters=" + queryParameters.toString() + ", version=" + version + "]";
    }
}
