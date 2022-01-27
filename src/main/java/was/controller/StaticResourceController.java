package was.controller;

import was.http.HttpRequest;
import was.http.HttpResponse;
import was.meta.HttpHeader;
import was.meta.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import was.meta.MediaType;
import was.meta.UrlPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class StaticResourceController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(StaticResourceController.class);

    private static final String DEFAULT_PATH = "src/main/resources/static";

    private final Map<String, byte[]> resources = new HashMap<>();

    private StaticResourceController() {
        final File DEFAULT_DIRECTORY = new File(DEFAULT_PATH);

        Queue<File> queue = new LinkedList<>(Arrays.asList(Objects.requireNonNull(DEFAULT_DIRECTORY.listFiles())));

        while (!queue.isEmpty()) {
            File file = queue.poll();
            if (file.isDirectory()) {
                queue.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
                continue;
            }

            try {
                resources.put(file.getPath(), Files.readAllBytes(file.toPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class InnerStaticResourceController {
        private static final StaticResourceController instance = new StaticResourceController();
    }

    public static StaticResourceController getInstance() {
        return InnerStaticResourceController.instance;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        try {
            String path = DEFAULT_PATH + request.getPath();
            byte[] body = resources.get(path);
            String contentType = MediaType.getMediaType(path, request.getHeader(HttpHeader.ACCEPT)).getValue();
            String contentLength = String.valueOf(body.length);

            response.setStatus(HttpStatus.OK);
            response.addHeader(HttpHeader.CONTENT_TYPE, contentType);
            response.addHeader(HttpHeader.CONTENT_LENGTH, contentLength);
            response.setBody(body);
        } catch (NullPointerException e) {
            e.printStackTrace();

            byte[] fail = resources.get(DEFAULT_PATH + UrlPath.ERROR_NOT_FOUND.getPath());
            String contentType = MediaType.TEXT_HTML.getValue();
            String contentLength = String.valueOf(fail.length);

            response.setStatus(HttpStatus.NOT_FOUND);
            response.addHeader(HttpHeader.CONTENT_TYPE, contentType);
            response.addHeader(HttpHeader.CONTENT_LENGTH, contentLength);
            response.setBody(fail);
        }
    }
}
