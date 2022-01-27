package webserver;

import controller.ControllerMapper;
import http.HttpHeader;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.RequestParameters;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;

public class RequestHandler extends Thread {

    private static final int REQUEST_METHOD_INDEX = 0;
    private static final int REQUEST_PATH_INDEX = 1;
    private static final int REQUEST_VERSION_INDEX = 2;
    private static final String WHITE_SPACE = " ";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ControllerMapper mapper;

    public static RequestHandler of(Socket connection) {
        return new RequestHandler(connection, ControllerMapper.create());
    }

    private RequestHandler(Socket connection, ControllerMapper mapper) {
        this.connection = connection;
        this.mapper = mapper;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (BufferedReader br =
            new BufferedReader(new InputStreamReader(connection.getInputStream()));
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {

            HttpRequest httpRequest = parseRequest(br);
            HttpResponse httpResponse = mapper.handleRequest(httpRequest);

            sendResponse(dos, httpRequest, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private HttpRequest parseRequest(BufferedReader br) throws IOException {
        String buffer = br.readLine();
        log.debug("HTTP Request Start Line :: {}", buffer);

        String[] requestTokens = HttpRequestUtils.parseRequestLine(buffer);
        HttpMethod method = HttpMethod.valueOf(requestTokens[REQUEST_METHOD_INDEX]);
        String path = requestTokens[REQUEST_PATH_INDEX];
        String version = requestTokens[REQUEST_VERSION_INDEX];

        String[] parsedPath = HttpRequestUtils.parsePath(path);
        Map<String, String> pathQueryString = Map.of();
        if (parsedPath.length == 2) {
            path = parsedPath[0];
            pathQueryString = HttpRequestUtils.parseQueryString(parsedPath[1]);
        }

        Map<String, String> headers = new HashMap<>();
        while ((buffer = br.readLine()) != null && !buffer.isBlank()) {
            log.debug("HTTP Request Header :: {}", buffer);
            Pair header = HttpRequestUtils.parseHeader(buffer);
            headers.put(header.getKey(), header.getValue());
        }

        int length = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
        String body = IOUtils.readData(br, length);
        log.debug("HTTP Request Body :: {}", body);

        return HttpRequest.builder()
            .method(method)
            .path(path)
            .version(version)
            .header(HttpHeader.of(headers))
            .pathParameters(RequestParameters.of(pathQueryString))
            .bodyParameters(RequestParameters.of(HttpRequestUtils.parseQueryString(body)))
            .build();
    }

    private void sendResponse(DataOutputStream dos, HttpRequest httpRequest,
        HttpResponse httpResponse) throws IOException {
        dos.writeBytes(httpRequest.getVersion() + WHITE_SPACE + httpResponse.respondStatus()
            + System.lineSeparator());
        dos.writeBytes(httpResponse.respondHeader() + System.lineSeparator());
        dos.writeBytes(System.lineSeparator());

        if (httpResponse.getResponseBody() != null) {
            dos.write(httpResponse.getResponseBody());
        }
        dos.flush();
    }
}
