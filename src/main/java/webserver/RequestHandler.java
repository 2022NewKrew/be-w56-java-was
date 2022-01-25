package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtils;
import util.HttpResponseUtil;
import webserver.http.HttpHeader;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;
import webserver.http.MimeType;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = HttpRequest.create(reader.readLine());
            log.info("[request]: {} {}", request.getMethod(), request.getUri());

            File contentFile = new File(WebServerConfig.BASE_PATH + request.getUri());
            String fileExtension = FileUtils.parseExtension(contentFile);

            byte[] contents = Files.readAllBytes(contentFile.toPath());
            HttpResponse response = new HttpResponse(HttpResponseStatus.OK, contents);
            response.headers()
                .set(HttpHeader.CONTENT_TYPE, MimeType.getExtensionHeaderValue(fileExtension))
                .set(HttpHeader.CONTENT_LENGTH, String.valueOf(contents.length));
            
            respond(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void respond(OutputStream out, HttpResponse response) {
        try {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(HttpResponseUtil.write(response));
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
