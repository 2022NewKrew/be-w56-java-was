package http;

import static http.HttpHeaders.ACCEPT;
import static http.HttpHeaders.CONTENT_TYPE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class HttpRequest {

    private HttpMethod httpMethod;
    private HttpVersion version;
    private HttpHeaders headers;
    private String path;
    private String body;
    private String view;
    private HttpStatus status;

    private HttpRequest(HttpMethod httpMethod, HttpVersion version, HttpHeaders headers,
        String path, String body, String view, HttpStatus status) {
        this.httpMethod = httpMethod;
        this.version = version;
        this.headers = headers;
        this.path = path;
        this.body = body;
        this.view = view;
        this.status = status;
    }

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        parseRequestLine(br);
        parseHeaders(br);
        view = path;
        status = HttpStatus.OK;
    }

    private void parseRequestLine(BufferedReader br) throws IOException {
        String[] tokens = br.readLine().split(" ");

        if (tokens.length != 3) {
            throw new IllegalArgumentException();
        }

        httpMethod = HttpMethod.resolve(tokens[0]);
        path = tokens[1];
        version = HttpVersion.of(tokens[2]);
    }

    private void parseHeaders(BufferedReader br) throws IOException {
        Map<String, String> headerMap = new TreeMap<>();
        String header;
        while (!(header = br.readLine()).equals("")) {
            Pair pair = HttpRequestUtils.parseHeader(header);
            headerMap.put(pair.getKey(), pair.getValue());
        }
        headers = HttpHeaders.of(headerMap);
    }

    public HttpResponse respond() {
        Map<String, String> headers = new TreeMap<>();
        headers.put(ACCEPT, this.headers.getAccept());
        return new HttpResponse(version, HttpStatus.OK, HttpHeaders.of(headers), view, null);
    }

    public Url getUrl() {
        return new Url(httpMethod, path);
    }

    public void postProcessing(String view) {
        this.view = view;
        this.headers.put(CONTENT_TYPE, "text/html;charset=utf-8");
        status = HttpStatus.OK;
    }
}
