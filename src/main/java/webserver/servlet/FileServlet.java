package webserver.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import webserver.http.HttpHeader;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.http.MimeType;
import webserver.util.FileUtils;

public class FileServlet implements FileResponsible {

    @Override
    public HttpResponse write(HttpResponse response, File file) throws IOException {
        String fileExtension = FileUtils.parseExtension(file);
        byte[] contents = Files.readAllBytes(file.toPath());
        response.setStatus(HttpResponseStatus.OK).setBody(contents);
        response.headers()
            .set(HttpHeader.CONTENT_TYPE, MimeType.getMimeSubtype(fileExtension))
            .set(HttpHeader.CONTENT_LENGTH, String.valueOf(contents.length));
        return response;
    }
}
