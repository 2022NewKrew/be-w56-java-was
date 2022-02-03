package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String httpMethod;
    private String respContextType;
    private String filePath;
    private String cookie;
    private byte[] body;

    public static Response of(Request request, String urlPath, byte[] body) {
        String filePath = fixFilePath(urlPath);
        return Response.builder()
                .httpMethod(request.getHttpMethod())
                .respContextType(request.getRespContextType())
                .filePath(filePath)
                .body(body)
                .cookie("")
                .build();
    }
    private static String fixFilePath(String urlPath) {
        return urlPath.contains(".") ? urlPath : urlPath + ".html";
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
