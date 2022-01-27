package webserver.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

public class HttpRequestBody {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestBody.class);


    private final String body;

    public HttpRequestBody(String body) {
        this.body = body;
    }

    public static HttpRequestBody createRequestBodyFromBufferedReader(BufferedReader br, int contentLength) throws IOException {
        String body = IOUtils.readData(br, contentLength);
        HttpRequestBody httpRequestBody = new HttpRequestBody(body);
        log.debug("Request Body(size:{}) decoded", httpRequestBody.getBodySize());
        return httpRequestBody;
    }

    public String getBody() {
        return body;
    }

    public long getBodySize() {
        return body.length();
    }
}
