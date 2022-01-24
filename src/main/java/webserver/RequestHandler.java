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
import util.HttpResponseUtil;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpResponseStatus;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try {
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String[] tokens = reader.readLine().split(" ");
            HttpRequest request = new HttpRequest(HttpMethod.valueOf(tokens[0]), tokens[1]);
            log.info("request {} {}", tokens[0], tokens[1]);

            HttpResponse response = new HttpResponse(
                HttpResponseStatus.OK,
                Files.readAllBytes(new File(WebServerConfig.BASE_PATH + request.getUri()).toPath())
            );
            respond(connection.getOutputStream(), response);
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
