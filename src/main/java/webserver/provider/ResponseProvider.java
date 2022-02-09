package webserver.provider;

import webserver.exception.UserUnauthorizedException;
import webserver.exception.WebServerException;
import webserver.http.HttpStatus;
import webserver.http.MIME;
import webserver.http.HttpResponse;

import java.io.IOException;

import static util.TemplateEngineUtils.renderDynamicTemplate;

public class ResponseProvider {

    public static HttpResponse responseStaticResource(String path) {
        try {
            byte[] body = StaticResourceProvider.getBytesFromPath(path);
            MIME mime = MIME.from(path);

            return HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .contentType(mime.getContentType())
                    .body(body)
                    .build();
        } catch (IOException e) {
            throw new WebServerException();
        }
    }

    public static HttpResponse responseClientException(WebServerException e) {
        if (e instanceof UserUnauthorizedException) {
            return response401Unauthorized(e);
        }

        byte[] body = e.getMessage().getBytes();

        return HttpResponse.builder()
                .status(e.getHttpStatus())
                .body(body)
                .build();
    }

    public static HttpResponse responseServerException(Exception e) {

        if (e instanceof WebServerException) {
            WebServerException exception = (WebServerException) e;

            byte[] body = ("서버 내부 에러 발생 : " + exception.getMessage()).getBytes();

            return HttpResponse.builder()
                    .status(exception.getHttpStatus())
                    .body(body)
                    .build();
        }

        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage().getBytes())
                .build();
    }

    private static HttpResponse response401Unauthorized(Exception e) {
        byte[] body = renderDynamicTemplate(e.getMessage(), "/user/login_failed.html").getBytes();
        return HttpResponse.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .body(body)
                .build();
    }
}
