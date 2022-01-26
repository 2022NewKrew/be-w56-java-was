package Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class StaticFileController implements Controller {

    @Override
    public String process(HttpRequest request, HttpResponse response) throws IOException {
        File file = request.getFile();

        checkFileValidation(response, file);

        Path filePath = file.toPath();
        response.setContentTypeWithFilePath(filePath);

        long contentLength = file.length();
        response.setContentLength(contentLength);

        return file.getPath();
    }

    private void checkFileValidation(HttpResponse response, File file) throws IOException {
        if (!file.exists()) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            throw new FileNotFoundException("File Not Found for requested URL '" + file.getPath() + "'");
        }
        if (!file.canRead() || !file.isFile()) {
            response.setStatusCode(HttpURLConnection.HTTP_FORBIDDEN);
            throw new AccessDeniedException(file.getPath());
        }
    }
}
