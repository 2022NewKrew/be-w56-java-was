package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.http.HttpClientErrorException;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequest request = parseRequest(br);

            if (request == null) {
                throw new HttpClientErrorException(HttpStatus.BadRequest, "잘못된 요청입니다. 요청 형식을 확인하세요");
            }

            if (request.getMethod() == HttpMethod.GET && StaticResourceManager.has(request.getUrl())) {

                byte[] body = StaticResourceManager.getResource(request.getUrl());

                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
                return;
            }

            byte[] body = WebController.getInstance().route(request);

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpRequest parseRequest(BufferedReader br) throws IOException {

        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();

        String line = br.readLine();
        String[] requestLine = line.split(" ");

        HttpMethod method = HttpMethod.valueOf(requestLine[0]);
        String url = requestLine[1];
        Header header = new Header();
        header.save(br);

        int queryStringStartIdx = url.indexOf("\\?");
        Map<String, String> params = new HashMap<>();
        if(queryStringStartIdx != -1){
            params = HttpRequestUtils.parseQueryString(url.substring(queryStringStartIdx));
            url = url.substring(0, queryStringStartIdx);
        }

        Map<String, String> body = new HashMap<>();
        if(!method.equals(HttpMethod.GET)){
            body = HttpRequestUtils.parseQueryString(IOUtils.readData(br, Integer.parseInt(header.getAttribute("Content-Length"))));
        }

        return httpRequestBuilder
                .setUrl(url)
                .setMethod(method)
                .setHeader(header)
                .setParams(params)
                .setBody(body)
                .build();
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
