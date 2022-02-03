package webserver.framwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.framwork.core.FrontController;
import webserver.framwork.http.Header;
import webserver.framwork.http.HttpClientErrorException;
import webserver.framwork.http.request.HttpMethod;
import webserver.framwork.http.request.HttpRequest;
import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;
import webserver.util.HttpRequestUtils;
import webserver.util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final FrontController frontController = FrontController.getInstance();
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        HttpRequest request = new HttpRequest();
        HttpResponse response = new HttpResponse();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            try {
                parseRequest(request, in);
                frontController.process(request, response);
            } catch (HttpClientErrorException e) {
                response.setStatus(HttpStatus.BadRequest);
                sendResponse(out, response);
            } finally {
                sendResponse(out, response);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void parseRequest(HttpRequest request, InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        parseRequestLine(request, br);
        parseRequestHeader(request, br);
        parseRequestBody(request, br);
    }

    private void parseRequestLine(HttpRequest request, BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new HttpClientErrorException(HttpStatus.BadRequest, "잘못된 요청 형식입니다.");
        }
        log.info(line);
        String[] requestLine = line.split(" ");

        int methodIdx = 0;
        int urlIdx = 1;

        String url = requestLine[urlIdx];
        int queryStringStartIdx = url.indexOf("\\?");
        Map<String, String> params = new HashMap<>();
        if (queryStringStartIdx != -1) {
            params = HttpRequestUtils.parseQueryString(url.substring(queryStringStartIdx));
            url = url.substring(0, queryStringStartIdx);
        }
        request.setUrl(url);
        request.setParams(params);

        request.setMethod(HttpMethod.valueOf(requestLine[methodIdx]));
    }

    private void parseRequestHeader(HttpRequest request, BufferedReader br) throws IOException {
        Header header = new Header();
        String line = br.readLine();
        while (!line.equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            header.addValue(pair.getKey(), pair.getValue());
            line = br.readLine();
        }
        request.setHeader(header);
    }

    private void parseRequestBody(HttpRequest request, BufferedReader br) throws IOException {
        if (request.getMethod().equals(HttpMethod.GET)) {
            return;
        }

        int contentLength = Integer.parseInt(request.getHeader().getValue("Content-Length"));
        Map<String, String> body = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
        request.setBody(body);
    }

    public void sendResponse(OutputStream out, HttpResponse response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeBytes("HTTP/1.1 " + response.getStatus().getStatusCode() + " " + response.getStatus().getMessage() + "\r\n");
        for (Header.Pair pair : response.getHeader()) {
            dos.writeBytes(pair.getKey() + ": " + pair.getValue() + "\r\n");
        }
        dos.writeBytes("Content-Length: " + response.getBody().length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(response.getBody(), 0, response.getBody().length);
        dos.flush();
    }

}
