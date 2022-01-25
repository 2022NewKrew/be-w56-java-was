package webserver.request;

import java.util.Arrays;

public enum MethodType {
    GET("get"), POST("post")
    , PUT("put"), DELETE("delete");


    private String methodString;

    MethodType(String methodString) {
        this.methodString = methodString;
    }

    public static MethodType of(String methodString){
        return Arrays.stream(values())
                .filter(methodType -> methodType.methodString.equalsIgnoreCase(methodString))
                .findFirst()
                .orElseThrow();
    }
}
