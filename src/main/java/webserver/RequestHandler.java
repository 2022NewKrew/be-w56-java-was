package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Map<String, String> headerMap = new HashMap<>();

            addRequestLine(br, headerMap);
            addRequestHeader(br, headerMap);

            RequestMapper requestMapper = new RequestMapper(HttpMethod.match(headerMap.get("METHOD")), headerMap.get("PATH"));
            String result = requestMapper.match();
            ContentType contentType = HttpRequestUtils.parseExtension(result);
            byte[] body = Files.readAllBytes(new File("./webapp" + result).toPath());

            DataOutputStream dos = new DataOutputStream(out);
            ResponseHandler responseHandler = new ResponseHandler(dos);
            responseHandler.response(body, contentType);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void addRequestHeader(BufferedReader br, Map<String, String> headerMap) throws IOException {
        String headerLine;
        while(!(headerLine = br.readLine()).equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
            headerMap.put(pair.getKey(), pair.getValue());
        }
    }

    private void addRequestLine(BufferedReader br, Map<String, String> headerMap) throws IOException {
        String httpRequestLine = br.readLine();
        for(HttpRequestUtils.Pair pair : HttpRequestUtils.parseRequestLine(httpRequestLine)) {
            headerMap.put(pair.getKey(), pair.getValue());
        }
    }
}
