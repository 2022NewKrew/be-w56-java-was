package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParseRequest {
    private final Map<String, String> requestInfo = new HashMap<>();
    BufferedReader reader;

    public ParseRequest(BufferedReader br) {
        this.reader = br;
    }

    public Map<String, String> getRequestMap() throws IOException {
        // request header
        parseFirstLine(reader.readLine().split(" "));
        String message;
        while (!Objects.equals(message = reader.readLine(), "")) {
            HttpRequestUtils.Pair keyValue = HttpRequestUtils.parseHeader(message);
            requestInfo.put(keyValue.getKey(), keyValue.getValue());
        }
        // request body
        int bodyLength = Integer.parseInt(requestInfo.getOrDefault("Content-Length", "0"));
        char[] body = new char[bodyLength];
        reader.read(body);
        String decodedString = URLDecoder.decode(new String(body), StandardCharsets.UTF_8);
        requestInfo.put("body", decodedString);
        return requestInfo;
    }

    private void parseFirstLine(String[] firstLine) {
        requestInfo.put("method", firstLine[0]);
        requestInfo.put("target", firstLine[1]);
    }
}
