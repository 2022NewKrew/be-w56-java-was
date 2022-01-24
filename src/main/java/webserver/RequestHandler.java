package webserver;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestMethod;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Slf4j
public class RequestHandler extends Thread {

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        ) {
            HttpRequest request = readHttpRequest(br);
            log.debug(request.toString());

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + request.getTarget()).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /* ---------------------------------------------------------------------- */
    private HttpRequest readHttpRequest(BufferedReader br) throws IOException {
        String line;
        String[] tokens;

        line = br.readLine();
        tokens = line.split(" ");
        if (tokens.length != 3) {
            throw new IOException();
        }

        HttpRequest request = HttpRequest.builder()
                .method(HttpRequestMethod.valueOf(tokens[0]))
                .target(tokens[1])
                .version(tokens[2])
                .build();

        // TODO: body 있는 경우 처리 추가
        while (true) {
            line = br.readLine();
            if (Strings.isNullOrEmpty(line)) {
                break;
            }

            tokens = line.split(": ");
            if (tokens.length != 2) {
                throw new IOException();
            }

            request.addHeader(tokens[0], tokens[1]);
        }

        return request;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
