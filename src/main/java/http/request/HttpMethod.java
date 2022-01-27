package http.request;

import java.util.Arrays;

public enum HttpMethod {

    GET("GET"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE");

    private String methodName;

    HttpMethod(String methodName) {
        this.methodName = methodName;
    }

    public static HttpMethod getHttpMethodByMethodName(String methodName) {
        return Arrays.stream(HttpMethod.values())
                .filter(httpMethod -> httpMethod.getMethodName().equals(methodName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 HttpMethod 입니다."));
    }

    private String getMethodName() {
        return methodName;
    }
}
