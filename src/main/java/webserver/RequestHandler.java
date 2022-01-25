package webserver;

import com.google.common.io.Files;
import http.response.HttpResponse;
import http.response.StatusCode;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            byte[] bytes = inputStreamToStrings(in);
            String request = new String(bytes, StandardCharsets.UTF_8);
            String url = parseUrl(request);
            Map<String, String> model = new HashMap<>();
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;
            // 정적 파일 리턴
            body = getStaticFile("./webapp" + url);

            HttpResponse response = new HttpResponse(dos, StatusCode.OK, url, body);
            response.sendResponse();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] inputStreamToStrings(InputStream inputStream) throws IOException {
        byte[] request = new byte[8186];
        inputStream.read(request);
        log.info(new String(request, StandardCharsets.UTF_8));
        return request;
    }

    private String parseUrl(String request) {
        String[] lines = request.split("\r\n");
        String[] elements = lines[0].split(" ");
        return elements[1];
    }

    private byte[] getStaticFile(String path) throws IOException {
        return Files.toByteArray(new File(path));
    }
}
