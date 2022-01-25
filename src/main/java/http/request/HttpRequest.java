package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {
    private final HttpRequestHeader header;
    private final HttpRequestLine line;
    // HttpRequestBody body;

    public HttpRequest(HttpRequestHeader header, HttpRequestLine line) {
        this.header = header;
        this.line = line;
    }

    public static HttpRequest readWithBufferedReader(BufferedReader br) throws IOException{
        HttpRequestHeader requestHeader = null;
        HttpRequestLine requestLine = null;

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

        return new HttpRequest(requestHeader, requestLine);
    }

    public HttpRequestHeader header() { return header; }

    public HttpRequestLine line() { return line; }
}
