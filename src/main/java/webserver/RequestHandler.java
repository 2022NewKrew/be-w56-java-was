package webserver;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.request.RequestContext;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseHeader;
import webserver.response.HttpResponseLine;
import webserver.response.ResponseContext;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (
                InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                DataOutputStream dos = new DataOutputStream(out)
        ) {
            createRequest(br);
            HttpResponse response = getResponse();
            response(response, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            RequestContext.getInstance().endRequest();
            ResponseContext.getInstance().endResponse();
        }
    }

    private HttpRequest createRequest(BufferedReader br) throws IOException {
        return RequestContext.getInstance().startRequest(br);
    }

    private HttpResponse getResponse() {
        return ResponseProcessor.getInstance().process();
    }

    private void response(HttpResponse response, DataOutputStream dos) throws IOException {
        writeResponseLine(dos, response.getResponseLine());
        writeResponseHeader(dos, response.getResponseHeader());
        byte[] body = response.getBytesForBodyContent();
        responseBody(dos, body);
    }

    private void writeResponseLine(DataOutputStream dos, HttpResponseLine responseLine) throws IOException {
        dos.writeBytes(responseLine.getHttpVersion() + StringUtils.SPACE + responseLine.getStatusCode() + StringUtils.CR + StringUtils.LF);
    }

    private void writeResponseHeader(DataOutputStream dos, HttpResponseHeader responseHeader) throws IOException {
        dos.writeBytes(responseHeader.getHeaders() + StringUtils.CR + StringUtils.LF);
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
