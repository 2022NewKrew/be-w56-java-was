package util;

import network.HttpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpBuilder {

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
}
