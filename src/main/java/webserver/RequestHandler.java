package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestParser;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
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
        try {
            File file = request.findStaticFile();
            Body body = Body.create(file);
            StatusLine statusLine = new StatusLine(HttpVersion.V_1_1, HttpStatus.OK);
            Headers responseHeaders = body.createResponseHeader();
            return new ResponseMessage(statusLine, responseHeaders, body);
        } catch (IOException exception) {
            StatusLine statusLine = new StatusLine(HttpVersion.V_1_1, HttpStatus.NOT_FOUND);
            Body body = new Body(new byte[]{});
            Headers responseHeaders = body.createResponseHeader();
            return new ResponseMessage(statusLine, responseHeaders, null);
        }
    }

    private RequestMessage receiveRequestMessage(InputStream in) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = IOUtils.readStartLine(buffer);
        List<String> header = IOUtils.readHeader(buffer);

        RequestLine startLine = HttpRequestParser.parseStartLine(line);
        Headers requestHeader = HttpRequestParser.parseHeaders(header);
        return new RequestMessage(startLine, requestHeader);
    }

    private void sendResponseMessage(OutputStream out, ResponseMessage response) {
        DataOutputStream dos = new DataOutputStream(out);
        IOUtils.writeResponse(dos, response);
    }
}