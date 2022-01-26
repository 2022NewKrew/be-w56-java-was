package controller;

import http.HttpHeader;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaticResourceController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(StaticResourceController.class);
    private static final Tika tika = new Tika();

    private static final String WEB_APP_PATH = "./webapp";

    public static StaticResourceController create() {
        return new StaticResourceController();
    }

    @Override
    public HttpResponse doGet(HttpRequest request) {
        try {
            String mimeType = tika.detect(WEB_APP_PATH + request.getPath());
            byte[] body = readFile(WEB_APP_PATH + request.getPath());
            HttpHeader responseHeader = HttpHeader.of(Map.of("Content-Type", mimeType,
                "Content-Length", String.valueOf(body.length)));

            return HttpResponse.builder()
                .status(HttpStatus.OK)
                .header(responseHeader)
                .responseBody(body)
                .build();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return HttpResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .build();
        }
    }

    private byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
