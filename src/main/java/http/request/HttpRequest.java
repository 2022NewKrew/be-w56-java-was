package http.request;

import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {
    private final HttpRequestLine line;
    private final HttpRequestHeader header;
    private final HttpRequestBody body;

    public HttpRequest(HttpRequestLine line, HttpRequestHeader header, HttpRequestBody body) {
        this.line = line;
        this.header = header;
        this.body = body;
    }

    public static HttpRequest readWithBufferedReader(BufferedReader br) throws IOException{
        HttpRequestHeader requestHeader;
        HttpRequestLine requestLine;
        HttpRequestBody requestBody;

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

            // Read request body
            int contentLength;
            try {
                contentLength = Integer.parseInt(requestHeader.getIfPresent("Content-Length"));
                requestBody = new HttpRequestBody(IOUtils.readData(br, contentLength));
            } catch (NumberFormatException e) { // Request body is empty.
                requestBody = null;
            }

        } catch (IOException e) {
            throw new IOException("failed to read HttpRequest");
        }

        return new HttpRequest(requestLine, requestHeader, requestBody);
    }

    public HttpRequestLine line() { return line; }

    public HttpRequestHeader header() { return header; }

    public HttpRequestBody body() { return body; }
}
