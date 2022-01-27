package webserver.common;

public enum HttpMethod {
    GET, POST, UNKNOWN;

    public static HttpMethod findMethod(String name) {
        try {
            return HttpMethod.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return HttpMethod.UNKNOWN;
        }
    }
}
