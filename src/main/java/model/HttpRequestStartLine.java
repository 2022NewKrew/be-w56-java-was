package model;

import java.util.List;
import java.util.Objects;

public class HttpRequestStartLine {
    private final String method;
    private final String url;
    private final String version;

    private HttpRequestStartLine(String method, String url, String version) {
        this.method = method;
        this.url = url;
        this.version = version;
    }

    public static HttpRequestStartLine valueOf(List<String> parsed) {
        validateNullability(parsed);

        String method = parsed.get(0);
        String url = parsed.get(1);
        String version = parsed.get(2);

        validateMethod(method);
        url = initializeUrlToIndexPage(url);
        validateHttpVersion(version);
        return new HttpRequestStartLine(method, url, version);
    }

    private static void validateNullability(List<String> parsed) throws NullPointerException {
        if(parsed == null) {
            throw new NullPointerException("시작줄이 없습니다.");
        }

        parsed.forEach(e -> {
            if(e == null) throw new NullPointerException("유효하지 않은 시작줄입니다.");
        });
    }

    private static void validateMethod(String method) throws UnsupportedOperationException {
        if (!Objects.equals(method, "GET")) {
            throw new UnsupportedOperationException("현재는 지원되지 않는 메소드입니다.");
        }
    }

    private static String initializeUrlToIndexPage(String url) {
        return Objects.equals(url, "/") ? "/index.html" : url;
    }

    private static void validateHttpVersion(String version) throws IllegalArgumentException {
        String[] parsed = version.split("/");
        if (parsed[1] == null ||
                !(Objects.equals(parsed[1], "0.9") ||
                Objects.equals(parsed[1], "1.0") ||
                Objects.equals(parsed[1], "1.1") ||
                Objects.equals(parsed[1], "2.0") ||
                Objects.equals(parsed[1], "3.0"))) {
            throw new IllegalArgumentException("잘못된 http 버전입니다.");
        }
    }

    public String getUrl() {
        return this.url;
    }
}
