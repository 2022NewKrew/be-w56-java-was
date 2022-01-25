package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String FILE_ROOT_PATH = "./webapp";

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = readLine(br);
            if (line == null) {
                return;
            }
            String url = HttpRequestUtils.getUrl(line);
            List<Pair> headers = readHeader(br);
            byte[] body = Files.readAllBytes(new File(FILE_ROOT_PATH + url).toPath());
            DataOutputStream dos = new DataOutputStream(out);
            responseHeader(dos, headers, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String readLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.debug("request: {}", line);
        return line;
    }

    private List<Pair> readHeader(BufferedReader br) throws IOException {
        List<Pair> headers = new ArrayList<>();
        String header;
        while(!"".equals(header=br.readLine())){
            log.debug("header: {}", header);
            headers.add(HttpRequestUtils.parseHeader(header));
        }
        return headers;
    }

    private void responseHeader(DataOutputStream dos, List<Pair> headers, int bodyLength) {
        String contentType = getHeaderValue(headers,"Accept").split(",")[0];
        response200Header(dos, bodyLength, contentType);
    }

    private String getHeaderValue(List<Pair> headers, String key) {
        return headers.stream()
            .filter(h->h.getKey().equals(key))
            .findFirst()
            .get()
            .getValue();
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "; charset=utf-8\r\n");
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
