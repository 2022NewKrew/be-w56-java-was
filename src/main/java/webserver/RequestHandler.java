package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.Request;
import util.Response;

import static util.Constant.*;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            Request request = HttpRequestUtils.parseInput(in);
            Response response = request.makeResponse();
            sendResponse(dos, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) {
        try {
            dos.writeBytes(response.getStatusLine() + NEW_LINE);
            dos.writeBytes(CONTANT_TYPE + response.getContextType() + UTF_8 + NEW_LINE);
            dos.writeBytes(CONTANT_LENGTH + response.getBodyLength() + NEW_LINE);
            dos.writeBytes(NEW_LINE);
            dos.write(response.getBody(), 0, response.getBodyLength());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
