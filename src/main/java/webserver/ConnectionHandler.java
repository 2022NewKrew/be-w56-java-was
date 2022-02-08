package webserver;

import http.header.MimeType;
import http.message.RequestMessage;
import http.message.ResponseMessage;
import http.startline.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.ServletRequest;
import servlet.ServletResponse;
import servlet.container.ServletContainer;
import util.Mapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

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

    private ResponseMessage createResponseMessage(RequestMessage request) throws IOException {
        File file = request.createStaticFile();
        if (file.exists() && file.isFile()) {
            String path = file.getPath();
            return ResponseMessage.create(HttpStatus.OK, MimeType.matchOf(path), Files.readAllBytes(Path.of(path)), null);
        }
        ServletRequest servletRequest = Mapper.toServletRequest(request);
        ServletResponse servletResponse = new ServletResponse();
        return servletContainer.process(servletRequest, servletResponse);
    }
}