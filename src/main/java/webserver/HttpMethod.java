package webserver;

public enum HttpMethod {
    POST,
    GET,
    PUT,
    PATCH,
    DELETE;

    public static HttpMethod match(String method) {
        if(method.equals("POST")) {
            return POST;
        }
        if(method.equals("DELETE")) {
            return POST;
        }
        if(method.equals("PUT")) {
            return POST;
        }
        if(method.equals("PATCH")) {
            return POST;
        }

        return GET;
    }
}
