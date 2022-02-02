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
        String requestLine = bufferedReader.readLine();
        Map<String, String> requestTokens = HttpRequestUtils.parseRequestLine(requestLine);

        Map<String, String> requestHeaders = new HashMap<>();
        String header = bufferedReader.readLine();
        while (!"".equals(header)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(header);
            requestHeaders.put(pair.getKey(), pair.getValue());
            header = bufferedReader.readLine();
        }

        String requestBody = null;
        if (!requestTokens.get("method").equals("GET")) {
            int contentLength = Integer.parseInt(requestHeaders.get("Content-Length"));
            requestBody = IOUtils.readData(bufferedReader, contentLength);
        }

        return new HttpRequest(requestTokens, requestHeaders, requestBody);
    }

    public void response(HttpRequest httpRequest) throws IOException {
        HttpResponse httpResponse = HandlerMapper.requestMapping(httpRequest);
        List<String> headers = httpResponse.getHeaders();
        headers.forEach(this::writeHeader);

        byte[] body = httpResponse.getBody();
        dataOutputStream.write(body, 0, body.length);
        dataOutputStream.flush();
    }

    private void writeHeader(String header) {
        try {
            dataOutputStream.writeBytes(header);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
