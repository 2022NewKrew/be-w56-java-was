package webserver.provider;

import webserver.exception.WebServerException;
import webserver.http.HttpStatus;
import webserver.http.MIME;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseProvider {

    public static MyHttpResponse responseStaticResource(DataOutputStream dos, String path) {
        try {
            byte[] body = StaticResourceProvider.getBytesFromPath(path);
            MIME mime = MIME.from(path);

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .contentType(mime.getContentType())
                    .body(body)
                    .build();
        } catch (IOException e) {
            throw new WebServerException();
        }
    }

    public static MyHttpResponse responseClientException(DataOutputStream dos, WebServerException e) {
        byte[] body = e.getErrorMessage().getBytes();

        return MyHttpResponse.builder(dos)
                .status(e.getHttpStatus())
                .body(body)
                .build();
    }

    public static MyHttpResponse responseServerException(DataOutputStream dos, Exception e) {
        if (e instanceof WebServerException) {
            WebServerException exception = (WebServerException) e;

            byte[] body = ("서버 내부 에러 발생 : " + exception.getErrorMessage()).getBytes();

            return MyHttpResponse.builder(dos)
                    .status(exception.getHttpStatus())
                    .body(body)
                    .build();
        }

        return MyHttpResponse.builder(dos)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage().getBytes())
                .build();
    }
}
