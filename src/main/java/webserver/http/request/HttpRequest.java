package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        System.out.println("parseRequestLine " + line);
        Map<String, String> request = parseRequest(line);
        return new HttpRequestLine(request.get(Constants.HTTP_METHOD), request.get(Constants.HTTP_URL), request.get(Constants.HTTP_VERSION));
    }

    private HttpRequestHeader parseRequestHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        System.out.println("parseRequestHeader " + line);
        List<Pair> pairs = new ArrayList<Pair>();

        while (!(line = br.readLine()).equals(Constants.EMPTY)) {
            System.out.println("HttpRequestHeader " + line);
            Pair pair = parseHeader(line);
            pairs.add(pair);
        }
        return new HttpRequestHeader(pairs);
    }

    private String parseRequestBody(BufferedReader br) throws IOException {
        Optional<Pair> contentLengthHeader = this.httpRequestHeader.getHeaders().stream()
                .filter(header -> header.getKey().equals("Content-Length"))
                .findAny();
       return contentLengthHeader.isEmpty()
                ? null
                : IOUtils.readData(br, Integer.parseInt(contentLengthHeader.get().getValue()));
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
}
