package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class StaticFileController implements Controller {

    private static final String ROOT_PATH_OF_WEB_RESOURCE_FILES = "./webapp";

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        File file = getFileFromUrl(request.getUri());

        validateFile(file);

        Path filePath = file.toPath();
        response.setContentTypeWithFilePath(filePath);

        long contentLength = file.length();
        response.setContentLength(contentLength);

        response.setViewPage(file.getPath());
    }

    private File getFileFromUrl(URI uri) {
        return new File(ROOT_PATH_OF_WEB_RESOURCE_FILES + uri.getPath());
    }

    private void validateFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File Not Found for requested URL '" + file.getPath() + "'");
        }
        if (!file.canRead() || !file.isFile()) {
            throw new AccessDeniedException(file.getPath());
        }
    }
}
