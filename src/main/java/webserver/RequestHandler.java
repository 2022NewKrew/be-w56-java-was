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
import webserver.domain.HttpStatus;
import webserver.domain.Request;
import webserver.domain.Response;

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
            DataOutputStream dos = new DataOutputStream(out);
            Response response = readRequestToResponse(br);
            dos.write(response.getBytes());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Response readRequestToResponse(BufferedReader br) throws IOException {
        Request request = createRequest(br);
        byte[] body = Files.readAllBytes(new File(request.getRequestUrl()).toPath());
        return Response.createResponse(HttpStatus.OK, body, request.getHeaderAttribute("Accept"));
    }

    public Request createRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.debug("request: {}", line);
        List<Pair> headerPairs = readHeader(br);
        return Request.createRequest(line, headerPairs);
    }

    private List<Pair> readHeader(BufferedReader br) throws IOException {
        List<Pair> headerPairs = new ArrayList<>();
        String header;
        while (!"".equals(header = br.readLine())) {
            log.debug("header: {}", header);
            Pair headerPair = HttpRequestUtils.parseHeader(header);
            headerPairs.add(headerPair);
        }
        return headerPairs;
    }
}
