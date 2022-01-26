package webserver;

import webserver.exception.WebServerException;
import webserver.http.HttpStatus;
import webserver.http.MIME;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseProvider {

    public static MyHttpResponse responseStaticResource(DataOutputStream dos, String path) {
        try {
            byte[] body = StaticResourceManager.getBytesFromPath(path);
            MIME mime = MIME.from(path);

            return MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .contentType(mime.getContentType())
                    .body(body)
                    .build();
        } catch (IOException e) {
            throw new WebServerException(dos);
        }
    }

    public static MyHttpResponse responseClientException(WebServerException e) {
        byte[] body = e.getErrorMessage().getBytes();

        return MyHttpResponse.builder(e.getDos())
                .status(e.getHttpStatus())
                .body(body)
                .build();
    }

    public static MyHttpResponse responseServerException(WebServerException e) {
        byte[] body = ("서버 내부 에러 발생 : " + e.getErrorMessage()).getBytes();

        return MyHttpResponse.builder(e.getDos())
                .status(e.getHttpStatus())
                .body(body)
                .build();
    }
}
