package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {
    private final HttpRequestLine line;
    private final HttpRequestHeader header;
    // HttpRequestBody body;

    public HttpRequest(HttpRequestLine line, HttpRequestHeader header) {
        this.line = line;
        this.header = header;
    }

    public static HttpRequest readWithBufferedReader(BufferedReader br) throws IOException{
        HttpRequestHeader requestHeader;
        HttpRequestLine requestLine;

        try {
            // Read request line
            String line = br.readLine();
            requestLine = HttpRequestLine.parseRequestLine(line);

            // Read request header
            List<String> headerArr = new ArrayList<>();
            while (!"".equals(line)) {
                line = br.readLine();
                headerArr.add(line);
            }
            requestHeader = HttpRequestHeader.parseHeader(headerArr);
        } catch (IOException e) {
            throw new IOException("tmp msg"); // TODO
        }

        return new HttpRequest(requestLine, requestHeader);
    }

    public HttpRequestHeader header() { return header; }

    public HttpRequestLine line() { return line; }
}
