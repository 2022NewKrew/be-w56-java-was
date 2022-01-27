package util.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

import static util.request.HttpRequestParser.*;

public class HttpRequestReader implements AutoCloseable{
    private static final Logger log = LoggerFactory.getLogger(HttpRequestReader.class);

    private BufferedReader br;

    public HttpRequestReader(InputStream in) {
        this.br = new BufferedReader(new InputStreamReader(in));
    }

    public HttpRequest read() throws IOException {
        String requestLine = IOUtils.readLine(br);
        Map<String, String> headers = IOUtils.readHeader(br);

        String body = null;
        if(headers.containsKey("Content-Length")){
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            body = IOUtils.readBody(br, contentLength);
        }

        return HttpRequest.builder()
                .method(parsingMethod(requestLine))
                .url(parsingUrl(requestLine))
                .queryParams(parsingQueryParams(requestLine))
                .httpVersion(parseHttpVersion(requestLine))
                .headers(headers)
                .body(body)
                .build();
    }

    @Override
    public void close() throws Exception {
        br.close();
    }
}
