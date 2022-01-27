package webserver.dto.request;

import com.google.common.base.Strings;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

import static webserver.common.exception.ExceptionMessage.*;

@Getter
@Builder
public class HttpRequestStartLine {
    private final HttpMethod method;
    private final String url;
    private final Map<String, String> queryParameters;
    private final String version;

    public static HttpRequestStartLine valueOf(
            String methodString,
            String url,
            String version,
            Map<String, String> queryParameters
    ) {
        validateUrl(url);
        validateHttpVersion(version);

        return HttpRequestStartLine.builder()
                .method(HttpMethod.of(methodString))
                .url(initializeUrlToIndexPage(url))
                .queryParameters(queryParameters)
                .version(version)
                .build();
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
