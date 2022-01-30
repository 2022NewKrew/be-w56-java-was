package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import controller.RequestController;
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
            writeResponse(new DataOutputStream(out), RequestController.controlRequest(requestHeader));
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private RequestHeader mapRequestHeader(BufferedReader bufferedReader) {
        RequestHeader requestHeader = new RequestHeader();
        try {
            HttpRequestUtils.setRequest(requestHeader, bufferedReader.readLine());

            bufferedReader.lines()
                    .takeWhile(header -> header.contains(": "))
                    .forEach(header -> HttpRequestUtils.setHeader(requestHeader, header));

            if (requestHeader.getHeader("method").equals("POST")) {
                String parameters = IOUtils.readData(bufferedReader,
                        Integer.parseInt(requestHeader.getHeader("Content-Length")));
                HttpRequestUtils.setRequestParameter(requestHeader, parameters);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return requestHeader;
    }

    private void writeResponse(DataOutputStream dos, ResponseHeader responseHeader) {
        responseHeader.getHtmlResponseHeader()
                .response(dos, responseHeader);
        responseHeader.responseBody(dos);
    }
}
