package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import com.google.common.collect.Maps;
import controller.FrontController;
import dto.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import static util.HttpRequestUtils.Pair;

public class RequestHandler extends Thread {
    private static final Logger log;
    private static final FrontController frontController;

    static {
        log = LoggerFactory.getLogger(RequestHandler.class);
        frontController = FrontController.getInstance();
    }

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader httpRequestReader = new BufferedReader(new InputStreamReader(in));

            RequestInfo requestInfo = this.parseRequestInfo(httpRequestReader);

            DataOutputStream dos = new DataOutputStream(out);
            frontController.dispatch(requestInfo, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private RequestInfo parseRequestInfo(BufferedReader httpRequestReader) throws IOException {
        String firstLine = httpRequestReader.readLine();
        String[] firstLineTokens = firstLine.split(" ");

        String requestMethod = firstLineTokens[0];
        String[] requestUrl = firstLineTokens[1].split("\\?");
        String requestPath = requestUrl[0];
        Map<String, String> queryParams = requestUrl.length == 2 ? HttpRequestUtils.parseQueryString(requestUrl[1]) : Maps.newHashMap();
        String version = firstLineTokens[2];


        Map<String, String> headers = this.parseHeader(httpRequestReader);
        Map<String, String> bodyParams = this.parseBody(httpRequestReader, Integer.parseInt(headers.getOrDefault("Content-Length", "0")));

        return RequestInfo.builder()
                          .requestMethod(requestMethod)
                          .requestPath(requestPath)
                          .version(version)
                          .queryParams(queryParams)
                          .headers(headers)
                          .bodyParams(bodyParams)
                          .build();
    }

    private Map<String, String> parseHeader(BufferedReader httpRequestReader) throws IOException {
        Map<String, String> headers = Maps.newHashMap();
        String line;
        while(!(line = httpRequestReader.readLine()).equals("")) {
            Pair keyAndValue = HttpRequestUtils.parseHeader(line);
            headers.put(keyAndValue.getKey(), keyAndValue.getValue());
        }

        return headers;
    }

    private Map<String, String> parseBody(BufferedReader httpRequestReader, int contentLength) throws IOException {
        if(contentLength == 0) {
            return Maps.newHashMap();
        }

        String requestBody = IOUtils.readData(httpRequestReader, contentLength);

        return HttpRequestUtils.parseQueryString(requestBody);
    }
}
