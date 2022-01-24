package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpClientErrorException;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;

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
            HttpRequest request = parseRequset(br);

            if (request == null){
                throw new HttpClientErrorException(HttpStatus.BadRequest, "잘못된 요청입니다. 요청 형식을 확인하세요");
            }

            if (request.getMethod()==HttpMethod.GET && StaticResourceManager.has(request.getUrl())){

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

    private HttpRequest parseRequset(BufferedReader br) throws IOException{
        String line = br.readLine();
        log.debug("request line : {}", line);

        String[] tokens = line.split(" ");
        HttpRequest request = null;
        if (tokens[0].equals("GET") && !tokens[1].equals("")) {
            request = new HttpRequest(HttpMethod.GET, tokens[1]);
        }

        while(!line.equals("")){
            line = br.readLine();
        }
        return request;
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
