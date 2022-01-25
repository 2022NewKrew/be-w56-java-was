package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            HttpRequest httpRequest = create(br);
            if (httpRequest.getUri().equals("/favicon.ico")) {
                return;
            }
            log.debug(httpRequest.toString());

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = FrontController.getView(httpRequest);
//            byte[] body = "Hello World".getBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);

            br.close();
            dos.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static HttpRequest create(BufferedReader br) throws IOException {

        String[] tokens = br.readLine().split(" ");
        Map<String, String> headers = new HashMap<>();
        String line = br.readLine();
        while (line != null && !line.equals("")) {
            headers.put(line.split(":")[0].trim(), line.split(":")[1].trim());
            line = br.readLine();
        }

//        String body = "";
//        line = br.readLine();
//        while (line != null && !line.equals("")) {
//            body += line + "\n";
//            line = br.readLine();
//        }

        return new HttpRequest(tokens[0], tokens[1], tokens[2], headers, "");
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
