package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.*;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final Method method;
    private final String url;
    private final String version;
    private final HttpRequestHeader httpRequestHeader;
    private final String httpRequestBody;

    public HttpRequest(InputStream in) throws IOException {
        log.debug("New HttpRequest: InputStream {}", in);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequestLine httpRequestLine = parseRequestLine(br);
        HttpRequestHeader httpRequestHeader = parseRequestHeader(br);
        this.method = Method.valueOf(httpRequestLine.getMethod());
        this.url = httpRequestLine.getUrl();
        this.version = httpRequestLine.getHttpVersion();
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = parseRequestBody(br);
    }

    private HttpRequestLine parseRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        Map<String, String> request = parseRequest(line);
        return new HttpRequestLine(request.get(Constants.HTTP_METHOD), request.get(Constants.HTTP_URL), request.get(Constants.HTTP_VERSION));
    }

    private HttpRequestHeader parseRequestHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        HashMap<String, String> pairs = new HashMap<>();

        while (!(line = br.readLine()).equals(Constants.EMPTY)) {
            Pair pair = parseHeader(line);
            pairs.put(pair.getKey(), pair.getValue());
        }
        return new HttpRequestHeader(pairs);
    }

    private String parseRequestBody(BufferedReader br) throws IOException {
        String contentLengthHeader = this.httpRequestHeader.getHeaders().get("Content-Length");
        return contentLengthHeader == null ? null : IOUtils.readData(br, Integer.parseInt(contentLengthHeader));
     }

    public Method getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpRequestBody() {
        return httpRequestBody;
    }

    public HttpRequestHeader getHttpRequestHeader() {
        return httpRequestHeader;
    }

}
