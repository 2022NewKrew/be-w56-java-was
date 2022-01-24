package webserver;

import java.util.Arrays;

public enum HttpMethod {
    GET,
    POST;

    public static HttpMethod parseHttpMethod(String method) {
        return Arrays.stream(HttpMethod.values())
                .filter(httpMethod -> httpMethod.name().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 Http Method 형식입니다. : " + method));
    }

}
