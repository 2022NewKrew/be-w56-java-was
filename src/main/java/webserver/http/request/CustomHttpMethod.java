package webserver.http.request;

public enum CustomHttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    String value;

    CustomHttpMethod(String value) {
        this.value = value;
    }

    public static boolean isHttpMethod(String method) {
        for(CustomHttpMethod c : CustomHttpMethod.values()) {
            if(c.value.equals(method)) {
                return true;
            }
        }
        return false;
    }

    public static CustomHttpMethod findHttpMethodByName(String name) {
        for(CustomHttpMethod c : CustomHttpMethod.values()) {
            if(c.value.equals(name)) {
                return c;
            }
        }
        return null;
    }
}
