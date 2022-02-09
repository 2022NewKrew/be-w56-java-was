package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import controller.RequestController;
import exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import model.RequestHeader;
import model.ResponseHeader;
import util.HttpRequestUtils;
import util.IOUtils;

@Slf4j
public class RequestHandler extends Thread {
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());


        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            RequestHeader requestHeader = mapRequestHeader(bufferedReader);
            writeResponse(new DataOutputStream(out), getResponseHeader(requestHeader));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private RequestHeader mapRequestHeader(BufferedReader bufferedReader) throws IOException {
        RequestHeader requestHeader = new RequestHeader();
        HttpRequestUtils.setRequest(requestHeader, bufferedReader.readLine());

        bufferedReader.lines()
                .takeWhile(header -> header.contains(": "))
                .forEach(header -> HttpRequestUtils.setHeader(requestHeader, header));

        if (requestHeader.getHeader("method").equals("POST")) {
            String parameters = IOUtils.readData(bufferedReader,
                    Integer.parseInt(requestHeader.getHeader("Content-Length")));
            HttpRequestUtils.setRequestParameter(requestHeader, parameters);
        }
        return requestHeader;
    }

    private void writeResponse(DataOutputStream dos, ResponseHeader responseHeader) throws IOException {
        responseHeader.getHtmlResponseHeader()
                .response(dos, responseHeader);
        responseHeader.responseBody(dos);
    }

    private ResponseHeader getResponseHeader(RequestHeader requestHeader) throws Exception {
        try {
            return RequestController.controlRequest(requestHeader);
        } catch (Exception exception) {
            return ExceptionHandler.handleException(exception, requestHeader);
        }
    }
}
