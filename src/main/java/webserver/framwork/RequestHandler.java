package webserver.framwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.framwork.core.FrontController;
import webserver.framwork.core.StaticResourceManager;
import webserver.framwork.http.Header;
import webserver.framwork.http.request.HttpMethod;
import webserver.framwork.http.request.HttpRequest;
import webserver.framwork.http.request.HttpRequestBuilder;
import webserver.framwork.http.response.HttpResponse;
import webserver.framwork.http.response.HttpStatus;

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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = parseRequest(in);
            HttpResponse response = new HttpResponse();

            if (request == null){
                response.setStatus(HttpStatus.BadRequest);
                sendResponse(out, response);
                return;
            }

            if (request.getMethod() == HttpMethod.GET && StaticResourceManager.has(request.getUrl())) {
                StaticResourceManager.forward(request, response);
            } else {
                frontController.process(request, response);
            }

            sendResponse(out, response);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();

        String line = br.readLine();
        log.debug(line);
        if (line == null) {
            return null;
        }
        String[] requestLine = line.split(" ");

        HttpMethod method = HttpMethod.valueOf(requestLine[0]);
        String url = requestLine[1];

        Header header = new Header();
        line = br.readLine();
        while (!line.equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            header.addValue(pair.getKey(), pair.getValue());
            line = br.readLine();
        }

        int queryStringStartIdx = url.indexOf("\\?");
        Map<String, String> params = new HashMap<>();
        if (queryStringStartIdx != -1) {
            params = HttpRequestUtils.parseQueryString(url.substring(queryStringStartIdx));
            url = url.substring(0, queryStringStartIdx);
        }

        Map<String, String> body = new HashMap<>();
        if (!method.equals(HttpMethod.GET)) {
            log.debug("Content-length === {}", Integer.parseInt(header.getValue("Content-Length")));
            body = HttpRequestUtils.parseQueryString(IOUtils.readData(br, Integer.parseInt(header.getValue("Content-Length"))));
        }

        return httpRequestBuilder
                .setUrl(url)
                .setMethod(method)
                .setHeader(header)
                .setParams(params)
                .setBody(body)
                .build();
    }

    public void sendResponse(OutputStream out, HttpResponse response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeBytes("HTTP/1.1 " + response.getStatus().getStatusCode() + " " + response.getStatus().getMessage() + "\r\n");
        for (String key : response.getHeader()) {
            dos.writeBytes(key + ": " + response.getHeaderValue(key) + "\r\n");
        }
        dos.writeBytes("Content-Length: " + response.getBody().length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(response.getBody(), 0, response.getBody().length);
        dos.flush();
    }

}
