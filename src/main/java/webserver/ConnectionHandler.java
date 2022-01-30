package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.ServletContainer;
import servlet.ServletRequest;
import util.*;

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
            RequestMessage request = receiveRequestMessage(in);
            ResponseMessage response = createResponseMessage(request);
            sendResponseMessage(out, response);
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

    private RequestMessage receiveRequestMessage(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = IOUtils.readStartLine(buffer);
        List<String> header = IOUtils.readHeader(buffer);

        RequestLine startLine = HttpRequestParser.parseStartLine(line);
        Headers requestHeader = HttpRequestParser.parseHeaders(header);

        String data = null;
        FieldName contentLength = new FieldName("Content-Length");
        if (requestHeader.getHeaders().containsKey(contentLength)) {
            data = IOUtils.readData(buffer, Integer.parseInt(requestHeader.getHeaders().get(contentLength).getValue()));
        }
        RequestBody requestBody = RequestBody.create(data);
        return new RequestMessage(startLine, requestHeader, requestBody);
    }

    private void sendResponseMessage(OutputStream out, ResponseMessage response) {
        DataOutputStream dos = new DataOutputStream(out);
        IOUtils.writeResponse(dos, response);
    }
}