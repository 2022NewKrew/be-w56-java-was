package model;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final byte[] NOT_FOUNT_MESSAGE = "없는 페이지 입니다.".getBytes();
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private static String parseExtension(String path) {
        List<String> splitResult = List.of(path.split("\\."));
        int length = splitResult.size();
        return splitResult.get(length - 1);
    }

    public static HttpResponse of(String path) {
        int statusCode = HttpStatus.OK.getCode();
        String statusMessage = HttpStatus.OK.getMessage();
        byte[] body = {};

        try {
            File file = new File("./webapp" + path);
            if (file.exists()) {
                body = Files.readAllBytes(file.toPath());
            } else {
                body = NOT_FOUNT_MESSAGE;
                statusCode = HttpStatus.NOT_FOUND.getCode();
                statusMessage = HttpStatus.NOT_FOUND.getMessage();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        StatusLine statusLine = new StatusLine(HttpVersion.HTTP_1_1.getVersion(), statusCode, statusMessage);
        Map<String, String> headerKeyMap = Map.of(
                "Content-Type", Mime.getMime(parseExtension(path)),
                "Content-Length", Integer.toString(body.length)
        );
        Header header = new Header(headerKeyMap);

        return new HttpResponse(statusLine, header, body);
    }

    private final StatusLine statusLine;
    private final Header header;
    private final byte[] body;

    private HttpResponse(StatusLine statusLine, Header header, byte[] body) {
        log.debug("HttpResponse: \n{}\n {}\n", statusLine, header);
        this.statusLine = statusLine;
        this.header = header;
        this.body = body;
    }

    public void sendResponse(Socket connection) {
        try (OutputStream out = connection.getOutputStream();) {
            DataOutputStream dos = new DataOutputStream(out);

            responseHeader(dos);
            responseBody(dos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseHeader(DataOutputStream dos) {
        try {
            dos.writeBytes(statusLine.message() + "\r\n");
            header.messageList().forEach(str -> {
                try {
                    dos.writeBytes(str + "\r\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
