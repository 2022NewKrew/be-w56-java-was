package webserver.servlet;

import com.google.common.net.HttpHeaders;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.http.MimeType;
import webserver.util.FileUtils;

public class FileHandler implements FileHandleable {

    @Override
    public void write(HttpResponse response, File file) throws IOException {
        String fileExtension = FileUtils.parseExtension(file);
        byte[] contents = Files.readAllBytes(file.toPath());
        response.setStatus(HttpResponseStatus.OK).setBody(contents);
        response.headers()
            .set(HttpHeaders.CONTENT_TYPE, MimeType.getMimeSubtype(fileExtension))
            .set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contents.length));
    }
}
