package webserver.processor.handler;

import com.google.common.io.Files;
import http.*;
import webserver.HttpFactory;
import webserver.file.DocumentRoot;

import java.net.URI;
import java.util.Map;

public class StaticFileHandler implements HttpHandler {

    @Override
    public boolean supports(HttpRequest httpRequest) {
        DocumentRoot documentRoot = HttpFactory.documentRoot();
        String requestPath = httpRequest.getPath();
        return httpRequest.equalsMethod(HttpMethod.GET) && documentRoot.existsFile(requestPath);
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        DocumentRoot documentRoot = HttpFactory.documentRoot();
        URI requestUri = httpRequest.getRequestUri();
        String extension = Files.getFileExtension(documentRoot.resolvePath(httpRequest.getPath()));
        MimeType mimeType = MimeType.getMimeTypeByExtension(extension);
        byte[] responseBody = documentRoot.readFileByPath(requestUri.getPath());
        return new HttpResponse(new HttpHeaders(Map.of("Content-Type", mimeType.getMimeTypeString())), StatusCode.OK, responseBody);
    }
}
