package webserver;

import lombok.extern.slf4j.Slf4j;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A socket connection thread to read and response to http request
 * <p>
 * The implementation is inevitably tied to Http Version.
 */
@Slf4j
public class ConnectionThread extends Thread {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    private final Socket connection;
    private final Dispatcher dispatcher;

    public ConnectionThread(Socket connectionSocket, Dispatcher dispatcher) {
        this.connection = connectionSocket;
        this.dispatcher = dispatcher;
    }

    /**
     * 1. Parse the http request from socket connection
     * <p>
     * 2. Create an empty http response for the request
     * <p>
     * 3. Dispatch the http request to appropriate handler to fill http response
     * <p>
     * 4. Post-process the response(e.g. configuring http header flags which is not decidable at dispatcher layer)
     * <p>
     * 5. Serialize the http response and send it to client
     */
    // Todo: Better multithreading through thread pool
    // Todo: Support persistent connection
    // How to implement persistent connection
    // https://stackoverflow.com/a/8067408
    public void run() {
        log.debug("New connection on {}:{}", connection.getInetAddress(), connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            HttpRequest httpRequest = HttpRequest.parseFrom(br);
            HttpResponse httpResponse = new HttpResponse(HTTP_VERSION, ENCODING);
            dispatcher.serve(httpRequest, httpResponse);
            dos.write(httpResponse.toByte());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
