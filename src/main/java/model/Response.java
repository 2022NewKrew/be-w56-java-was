package model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Response {
    private String httpMethod;
    private String contextType;
    private String urlPath;

    @Builder
    public Response(String httpMethod, String contextType, String urlPath){
        this.httpMethod = httpMethod;
        this.contextType = contextType;
        this.urlPath = urlPath;
    }

    public static Response of(String httpMethod, String contextType, String urlPath) {
        return Response.builder()
                .httpMethod(httpMethod)
                .contextType(contextType)
                .urlPath(urlPath)
                .build();
    }
}
