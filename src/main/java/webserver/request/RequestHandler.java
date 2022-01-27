package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.header.HttpMethod;
import webserver.header.RequestLine;
import webserver.response.ResponseHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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

        try ( BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            Map<String, String> headerMap = new HashMap<>();

            addRequestLine(br, headerMap);
            addRequestHeader(br, headerMap);
            Map<String, String> queryMap = getQueryStringMap(headerMap);
            Map<String, String> bodyMap = getRequestBody(br, headerMap);

            RequestControllerMatcher requestControllerMatcher = new RequestControllerMatcher(
                    HttpMethod.match(headerMap.get(RequestLine.METHOD.name())),
                    headerMap.get(RequestLine.PATH.name()));
            String result = requestControllerMatcher.match(queryMap, bodyMap);

            ResponseHandler responseHandler = new ResponseHandler(dos);
            responseHandler.response(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private Map<String, String> getRequestBody(BufferedReader br, Map<String, String> headerMap) throws IOException {
        Map<String, String> bodyMap = new HashMap<>();
        if (headerMap.get("Content-Length") != null) {
            String body = IOUtils.readData(br, Integer.parseInt(headerMap.get("Content-Length")));
            bodyMap = HttpRequestUtils.parseBody(body);
        }
        return bodyMap;
    }

    private Map<String, String> getQueryStringMap(Map<String, String> headerMap) {
        Map<String, String> queryMap = new HashMap<>();
        String[] parsedPath = HttpRequestUtils.parseGetRequest(headerMap.get("PATH"));
        if(parsedPath.length == 2) {
            queryMap = HttpRequestUtils.parseQueryString(parsedPath[1]);
            headerMap.put("PATH", parsedPath[0]);
        }
        return queryMap;
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
