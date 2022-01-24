package webserver;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.request.RequestContext;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseHeader;
import webserver.response.HttpResponseLine;

import java.io.*;
import java.net.Socket;

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
            HttpRequest httpRequest = createRequest(in);
            HttpResponse response = getResponse(httpRequest);
            response(response, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            RequestContext.getInstance().endRequest();
        }
    }

    private HttpRequest createRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        return RequestContext.getInstance().startRequest(br);
    }

    private HttpResponse getResponse(HttpRequest httpRequest) {
        return ResponseProcessor.getInstance().response(httpRequest);
    }

    private void response(HttpResponse response, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
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
