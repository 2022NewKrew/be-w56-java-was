package util.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import static util.request.HttpRequestUtils.*;
import static util.IOUtils.readData;

public class Request {
    private final RequestLine line;
    private final Map<String, String> header;
    private String body;

    public Request(BufferedReader br) throws IOException {
        line = parseRequestLine(br);
        header = readHeader(br);
        if (header.containsKey("Content-Length")) {
            body = readData(br,Integer.parseInt(header.get("Content-Length")));
        }
    }

    public RequestLine getLine() {
        return line;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
