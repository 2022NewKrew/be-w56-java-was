package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String httpMethod;
    private String respContextType;
    private String filePath;


    public static Response of(String httpMethod, String respContextType, String urlPath) {
        String filePath = fixFilePath(urlPath);
        return Response.builder()
                .httpMethod(httpMethod)
                .respContextType(respContextType)
                .filePath(filePath)
                .build();
    }

    private static String fixFilePath(String urlPath) {
        return urlPath.contains(".") ? urlPath : urlPath + ".html";
    }
}
