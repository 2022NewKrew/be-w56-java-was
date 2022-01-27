package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.http.MIME;
import webserver.http.PathInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseGenerator {
    private static final Logger log = LoggerFactory.getLogger(ResponseGenerator.class);

    public static HttpResponse generateStaticResponse(String path) {
        try {
            byte[] body = Files.readAllBytes(new File(PathInfo.URI_BASE + path).toPath());

            return HttpResponse.builder()
                    .status(HttpStatus.OK)
                    .contentType(MIME.parse(path))
                    .body(body)
                    .build();
        } catch (IOException e) {
            log.error(e.getMessage());
            return generateResponse500();
        }
    }

    public static HttpResponse generateLoginResponse() {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", PathInfo.PATH_INDEX)
                .header("Set-Cookie", "logined=true; Path=/")
                .build();
    }

    public static HttpResponse generateLoginFailedResponse() {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", PathInfo.PATH_LOGIN_FAILED)
                .header("Set-Cookie", "logined=false; Path=/")
                .build();
    }

    public static HttpResponse generateResponse302(String path) {
        return HttpResponse.builder()
                .status(HttpStatus.FOUND)
                .header("Location", path)
                .build();
    }

    public static HttpResponse generateResponse400(Exception e) {
        byte[] body = (HttpStatus.BAD_REQUEST + ": " + e.getMessage()).getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .body(body)
                .build();
    }

    public static HttpResponse generateResponse404() {
        byte[] body = HttpStatus.NOT_FOUND.toString().getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .body(body)
                .build();
    }

    public static HttpResponse generateResponse500() {
        byte[] body = HttpStatus.INTERNAL_SERVER_ERROR.toString().getBytes();

        return HttpResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body)
                .build();
    }
}
