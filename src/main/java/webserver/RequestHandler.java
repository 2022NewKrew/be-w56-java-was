package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Charsets;
import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.request.HttpRequestBody;
import http.request.HttpRequestLine;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort()); //

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)); DataOutputStream dos = new DataOutputStream(out)) {

            HttpRequest httpRequest = getHttpRequest(br);
            handleResponse(httpRequest, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpRequest getHttpRequest(BufferedReader br) throws IOException {
        HttpRequestLine requestLine = getRequestLine(br);
        HttpHeaders requestHeader = getReqeustHeader(br);
        HttpRequestBody requestBody = getRequestBody(br, requestHeader);

        return new HttpRequest(requestLine, requestHeader, requestBody);
    }

    private HttpRequestLine getRequestLine(BufferedReader br) throws IOException {
        String line = "";
        if ((line = br.readLine()) == null) {
            throw new IllegalArgumentException("invalid http request");
        }
        log.info("request line = {}", line);
        return new HttpRequestLine(line);
    }

    private HttpHeaders getReqeustHeader(BufferedReader br) throws IOException {
        if (!br.ready()) {
            return null;
        }
        List<String> requestHeader = new ArrayList<>();
        String line = br.readLine();
        while (line != null && !line.equals("")) {
            log.info("request = {}", line);
            requestHeader.add(line);
            line = br.readLine();
        }

        return new HttpHeaders(requestHeader);
    }

    private HttpRequestBody getRequestBody(BufferedReader br, HttpHeaders headers) throws IOException {
        if (!br.ready() && !headers.containsKey(HttpHeaders.CONTENT_LENGTH)) {
            return null;
        }

        String len = headers.getFirst(HttpHeaders.CONTENT_LENGTH);
        char[] chars = new char[Integer.parseInt(len)];
        br.read(chars);

        return new HttpRequestBody(new String(chars));
    }

    private void handleResponse(HttpRequest request, DataOutputStream dos) throws IOException {
        HttpResponse response = new HttpResponse();
        Router.route(request, response);
        writeResponse(response, dos);
    }

    private void writeResponse(HttpResponse response, DataOutputStream dos) throws IOException {
        dos.write(response.getStatus());
        dos.write(response.getHeaders());
        dos.write(response.getBody());
        dos.flush();
    }
}
