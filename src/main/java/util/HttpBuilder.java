package util;

import network.HttpRequest;
import network.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpBuilder {

    private static final Logger log = LoggerFactory.getLogger(HttpBuilder.class);
    private final BufferedReader bufferedReader;
    private final DataOutputStream dataOutputStream;

    public HttpBuilder(BufferedReader bufferedReader, DataOutputStream dataOutputStream) {
        this.bufferedReader = bufferedReader;
        this.dataOutputStream = dataOutputStream;
    }

    public HttpRequest request() throws IOException {
        Map<String, String> requestTokens = readRequestTokens();
        Map<String, String> requestHeaders = readRequestHeaders();
        String requestBody = readRequestBody(requestTokens, requestHeaders);
        return new HttpRequest(requestTokens, requestHeaders, requestBody);
    }

    public void response(HttpRequest httpRequest) throws IOException {
        HttpResponse httpResponse = RequestMapper.requestMapping(httpRequest);
        List<String> headers = httpResponse.getHeaders();
        headers.forEach(this::writeResponseHeader);
        byte[] body = httpResponse.getBody();
        writeResponseBody(body);
    }

    private Map<String, String> readRequestTokens() throws IOException {
        String requestLine = bufferedReader.readLine();
        return HttpRequestUtils.parseRequestLine(requestLine);
    }

    private Map<String, String> readRequestHeaders() throws IOException {
        Map<String, String> requestHeaders = new HashMap<>();
        String header = bufferedReader.readLine();
        while (!"".equals(header)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(header);
            requestHeaders.put(pair.getKey(), pair.getValue());
            header = bufferedReader.readLine();
        }
        return requestHeaders;
    }

    private String readRequestBody(Map<String, String> requestTokens, Map<String, String> requestHeaders) throws IOException {
        String requestBody = null;
        if (!requestTokens.get("method").equals("GET")) {
            int contentLength = Integer.parseInt(requestHeaders.get("Content-Length"));
            requestBody = IOUtils.readData(bufferedReader, contentLength);
        }
        return requestBody;
    }

    private void writeResponseHeader(String header) {
        try {
            dataOutputStream.writeBytes(header);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeResponseBody(byte[] body) throws IOException {
        dataOutputStream.write(body, 0, body.length);
        dataOutputStream.flush();
    }
}
