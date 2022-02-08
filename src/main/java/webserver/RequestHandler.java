package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;
import webserver.domain.Request;
import webserver.domain.Response;

public class RequestHandler extends Thread {

    private static final String CONTENT_LENGTH = "Content-Length";

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
            DataOutputStream dos = new DataOutputStream(out);
            Response response = readRequestToResponse(br);
            dos.write(response.getBytes());
            dos.flush();
        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }
    }

    private Response readRequestToResponse(BufferedReader br)
        throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Request request = createRequest(br);
        return ControllerHandler.run(request);
    }

    public Request createRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.debug("request: {}", line);
        Map<String, String> headers = readHeader(br);
        String bodyString = readBody(br, headers);
        return Request.createRequest(line, headers, bodyString);
    }

    private Map<String, String> readHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String header;
        while (!"".equals(header = br.readLine())) {
            log.debug("header: {}", header);
            Pair headerPair = HttpRequestUtils.parseHeader(header);
            headers.put(headerPair.getKey(), headerPair.getValue());
        }
        return headers;
    }

    private String readBody(BufferedReader br, Map<String, String> headers) throws IOException {
        if (!headers.containsKey(CONTENT_LENGTH)) {
            return null;
        }
        int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH));
        String bodyString = IOUtils.readData(br, contentLength);
        log.debug("body: {}", bodyString);
        return bodyString;
    }
}
