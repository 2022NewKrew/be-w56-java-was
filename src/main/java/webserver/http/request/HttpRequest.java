package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final Method method;
    private final String url;
    private final String version;
//    private final Map<String, String> httpRequestHeader;

    public HttpRequest(InputStream in) throws IOException {
        log.debug("New HttpRequest: InputStream {}", in);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequestLine httpRequestLine = parseRequestLine(br);
        this.method = Method.valueOf(httpRequestLine.getMethod());
        this.url = httpRequestLine.getUrl();
        this.version = httpRequestLine.getHttpVersion();

    }

    private HttpRequestLine parseRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        Map<String, String> request = HttpRequestUtils.parseRequest(line);
        return new HttpRequestLine(request.get(Constants.HTTP_METHOD), request.get(Constants.HTTP_URL), request.get(Constants.HTTP_VERSION));
    }

    public Method getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
