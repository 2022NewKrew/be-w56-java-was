package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.controller.WebController;
import webserver.core.StaticResourceManager;
import webserver.core.http.Header;
import webserver.core.http.HttpClientErrorException;
import webserver.core.http.request.HttpMethod;
import webserver.core.http.request.HttpRequest;
import webserver.core.http.request.HttpRequestBuilder;
import webserver.core.http.response.HttpResponse;
import webserver.core.http.response.HttpStatus;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
            HttpRequest request = parseRequest(br);

            if (request == null) {
                throw new HttpClientErrorException(HttpStatus.BadRequest, "잘못된 요청입니다. 요청 형식을 확인하세요");
            }

            DataOutputStream dos = new DataOutputStream(out);
            if (request.getMethod() == HttpMethod.GET && StaticResourceManager.has(request.getUrl())) {
                StaticResourceManager.getResource(request).response(dos);
                return;
            }
            HttpResponse res = WebController.getInstance().route(request);
            res.response(dos);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpRequest parseRequest(BufferedReader br) throws IOException {

        HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();

        String line = br.readLine();
        if (line == null) {
            return null;
        }
        String[] requestLine = line.split(" ");

        HttpMethod method = HttpMethod.valueOf(requestLine[0]);
        String url = requestLine[1];

        Header header = new Header();
        line = br.readLine();
        while (!line.equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            header.addHeaderValue(pair.getKey(), pair.getValue());
            line = br.readLine();
        }

        int queryStringStartIdx = url.indexOf("\\?");
        Map<String, String> params = new HashMap<>();
        if (queryStringStartIdx != -1) {
            params = HttpRequestUtils.parseQueryString(url.substring(queryStringStartIdx));
            url = url.substring(0, queryStringStartIdx);
        }

        Map<String, String> body = new HashMap<>();
        if (!method.equals(HttpMethod.GET)) {
            body = HttpRequestUtils.parseQueryString(IOUtils.readData(br, Integer.parseInt(header.getHeaderValue("Content-Length"))));
        }

        return httpRequestBuilder
                .setUrl(url)
                .setMethod(method)
                .setHeader(header)
                .setParams(params)
                .setBody(body)
                .build();
    }

}
