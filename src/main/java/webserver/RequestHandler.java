package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestDecoder;
import webserver.http.request.exceptions.NullRequestException;
import webserver.http.response.EncodedHttpResponse;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseHeaders;
import webserver.http.response.HttpResponseHeadersEncoder;

public class RequestHandler extends Thread {

    private static final String ROOT_PATH_OF_WEB_RESOURCE_FILES = "./webapp";

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
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (NullRequestException | URISyntaxException e) {
            // TODO : Exception Handler required
            e.printStackTrace();
        }

    }

    private void handle(HttpRequest request, HttpResponse response) throws IOException {
        URI uri = request.getUri();

        File file = new File(ROOT_PATH_OF_WEB_RESOURCE_FILES + uri.getPath());

        if (!file.exists()) {
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            // TODO : Exception Handler required
        }

        if (!file.canRead()) {
            response.setStatusCode(HttpURLConnection.HTTP_FORBIDDEN);
            // TODO : Exception Handler required
        }

        handleFile(response, file);
    }

    private void handleFile(HttpResponse response, File file) throws IOException {
        Path filePath = file.toPath();
        response.setContentType(filePath);

        long contentLength = file.length();
        response.setContentLength(contentLength);

        EncodedHttpResponse encodedHttpResponse = HttpResponseHeadersEncoder.encode(response, filePath);
        response.sendResponse(encodedHttpResponse);
    }
}
