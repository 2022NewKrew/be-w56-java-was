package webserver.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class HttpRequestLine {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestLine.class);

    private final Method method;
    private final URI uri;
    private final String httpVersion;

    public HttpRequestLine(Method method, URI uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestLine createRequestLineFromBufferedReader(BufferedReader br) throws IOException {
        String httpRequestLineString = br.readLine();

        if (httpRequestLineString == null) {
            return null;
        }

        log.debug("Request Line : {}", httpRequestLineString);

        HttpRequestLine httpRequestLine = HttpRequestUtils.parseRequestLine(httpRequestLineString);

        log.debug(
            "Method: {} URI: {} Version: {} decoded",
            httpRequestLine.getMethod(),
            httpRequestLine.getUri(),
            httpRequestLine.getHttpVersion()
        );
        return httpRequestLine;
    }

    public Method getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
