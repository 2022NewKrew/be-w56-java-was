package util.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import static util.request.HttpRequestParser.*;

public class HttpRequestReader implements AutoCloseable{
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
                .bodyParams(parsingBodyParams(body))
                .build();
    }

    @Override
    public void close() throws Exception {
        br.close();
    }
}
