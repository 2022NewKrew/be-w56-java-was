package webserver;

import static webserver.http.HttpMeta.ROOT_PATH_OF_WEB_RESOURCE_FILES;
import static webserver.http.HttpMeta.VIEW_BASIC_PAGE;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.SignUpService;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestDecoder;
import webserver.http.response.EncodedHttpResponse;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseHeaders;
import webserver.http.response.HttpResponseHeadersEncoder;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug(
            "New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort()
        );

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest httpRequest = HttpRequestDecoder.decode(in);
            HttpResponse httpResponse = new HttpResponse(
                httpRequest.getHttpVersion(),
                new HttpResponseHeaders(),
                dos
            );

            handle(httpRequest, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void handle(HttpRequest request, HttpResponse response) throws IOException {
        String requestPath = request.getUri();
        URI uri;
        try {
            uri = new URI(requestPath);
        } catch (URISyntaxException e) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            handleError(response, e);
            return;
        }

        if (requestPath.startsWith("/user/create")) {
            handleQuery(response, uri);
            return;
        }

        File file = new File(ROOT_PATH_OF_WEB_RESOURCE_FILES + uri.getPath());

        if (!file.exists() || !file.isFile()) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            handleError(response, new FileNotFoundException("File Not Found for requested URI '" + uri + "'"));
            return;
        }
        if (!file.canRead()) {
            response.setStatusCode(HttpURLConnection.HTTP_FORBIDDEN);
            handleError(response, new AccessDeniedException(file.getPath()));
            return;
        }

        handleFile(response, file);
    }

    private void handleQuery(HttpResponse response, URI uri) throws IOException {
        SignUpService.signUp(uri.getQuery());
        response.setStatusCode(HttpURLConnection.HTTP_MOVED_TEMP);
        response.setLocation(VIEW_BASIC_PAGE);

        EncodedHttpResponse encodedHttpResponse = HttpResponseHeadersEncoder.encode(response, null);
        response.sendRedirectResponse(encodedHttpResponse);
    }

    private void handleFile(HttpResponse response, File file) throws IOException {
        Path filePath = file.toPath();
        response.setContentTypeWithFilePath(filePath);

        long contentLength = file.length();
        response.setContentLength(contentLength);

        EncodedHttpResponse encodedHttpResponse = HttpResponseHeadersEncoder.encode(response, filePath);
        response.sendNormalResponse(encodedHttpResponse);
    }

    private void handleError(HttpResponse response, Exception e) throws IOException {
        ExceptionHandler.handleException(response, e);
    }
}
