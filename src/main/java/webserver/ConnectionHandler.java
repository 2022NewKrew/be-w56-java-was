package webserver;

import http.HttpStatus;
import http.RequestMessage;
import http.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.ServletContainer;
import servlet.ServletRequest;
import util.Mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);

    private final Socket connection;
    private final ServletContainer servletContainer;

    public ConnectionHandler(Socket connectionSocket, ServletContainer servletContainer) {
        this.connection = connectionSocket;
        this.servletContainer = servletContainer;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}"
                , connection.getInetAddress()
                , connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            RequestMessage request = InputHandler.receiveRequestMessage(in);
            ResponseMessage response = createResponseMessage(request);
            OutputHandler.sendResponseMessage(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private ResponseMessage createResponseMessage(RequestMessage request) {
        byte[] file = StaticResourceContainer.process(request);
        if (file.length != 0) {
            return ResponseMessage.create(HttpStatus.OK, file);
        }
        ServletRequest servletRequest = Mapper.toServletRequest(request);
        return servletContainer.process(servletRequest);
    }
}