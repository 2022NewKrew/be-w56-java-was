package webserver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Controller.UserController;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private final String HTTP_REQUEST_200 = "HTTP/1.1 200 OK ";
    private final String CONTENT_TYPE = "Content-Type: ";
    private final String CHARSET = "charset=utf-8";
    private final String CONTENT_LENGTH = "Content-Length: ";
    private final String NEW_LINE = "\r\n";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        // Http 클래스 분리
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String request = bufferedReader.readLine();
            printHeader(request, bufferedReader);

            String[] splited = request.split(" ");
            String url = splited[1];
            String source = getSource(url);
            log.debug(source);
            byte[] response = getResponse(source);

            DataOutputStream dataOutputStream = new DataOutputStream(out);
            dataOutputStream.write(response, 0, response.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void printHeader(String request, BufferedReader bufferedReader) throws IOException {
        log.debug(request);
        String line;
        while(!(line=bufferedReader.readLine()).equals("")){
            log.debug(line);
        }
    }

    private byte[] getResponse(String url) {
        byte[] body = getBody(url);
        byte[] header = getHeader(url, body.length);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(header);
            outputStream.write(body);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
        return outputStream.toByteArray();
    }

    private byte[] getHeader(String url, int contentLength) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(HTTP_REQUEST_200).append(NEW_LINE);
        stringBuilder.append(CONTENT_TYPE).append(getContentType(url));
        stringBuilder.append(CHARSET).append(NEW_LINE);
        stringBuilder.append(CONTENT_LENGTH).append(contentLength).append(NEW_LINE).append(NEW_LINE);

        return stringBuilder.toString().getBytes();
    }

    private byte[] getBody(String url) {
        try {
            return Files.readAllBytes(new File("webapp/" + url).toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getContentType(String url) {
        if (url.contains("js")) {
            return "application/js;";
        }
        if (url.contains("css")) {
            return "text/css;";
        }
        return "text/html;";
    }

    private String getSource(String url) {
        String[] splited = url.split("\\?");
        String method = splited[0];
        log.debug(method);
        if (method.matches("/users(.*)")) {
            log.debug("hi: " + method);
            UserController userController = new UserController(method, HttpRequestUtils.parseQueryString(splited[1]));
            return userController.run(method);
        }
        if (method.matches("index.html")) {
            return "index.html";
        }
        return method;
    }
}
