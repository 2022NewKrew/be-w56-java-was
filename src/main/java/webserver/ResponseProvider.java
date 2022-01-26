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

    public static MyHttpResponse response404NotFound(WebServerException e) {
        byte[] body = HttpStatus.NOT_FOUND.toString().getBytes();

        return MyHttpResponse.builder(e.getDos())
                .status(HttpStatus.NOT_FOUND)
                .body(body)
                .build();
    }

    public static MyHttpResponse response400BadRequest(WebServerException e) {
        byte[] body = (HttpStatus.BAD_REQUEST + ":" + e.getMessage()).getBytes();

        return MyHttpResponse.builder(e.getDos())
                .status(HttpStatus.BAD_REQUEST)
                .body(body)
                .build();
    }

    public static MyHttpResponse response405MethodNotAllowed(WebServerException e) {
        byte[] body = HttpStatus.METHOD_NOT_ALLOWED.toString().getBytes();

        return MyHttpResponse.builder(e.getDos())
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(body)
                .build();
    }

    public static MyHttpResponse response500InternalServerError(WebServerException e) {
        byte[] body = HttpStatus.INTERNAL_SERVER_ERROR.toString().getBytes();

        return MyHttpResponse.builder(e.getDos())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body)
                .build();
    }

}
