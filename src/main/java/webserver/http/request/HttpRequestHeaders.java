package webserver.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class HttpRequestHeaders {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestHeaders.class);


    private final Map<String, String> headers = new HashMap<>();

    public static HttpRequestHeaders createRequestHeadersFromBufferedReader(BufferedReader br) throws IOException {
        HttpRequestHeaders requestHeaders = new HttpRequestHeaders();
        String line = br.readLine();

        while (!line.isEmpty()) {
            log.debug("Header : {}", line);

            Pair header = HttpRequestUtils.parseHeader(line);
            requestHeaders.addHeader(header.getKey(), header.getValue());

            line = br.readLine();
        }
        log.debug("Request Headers(size:{}}) decoded", requestHeaders.getSize());
        return requestHeaders;
    }

    private void addHeader(String httpRequestHeaderKey, String httpRequestHeaderValue) {
        headers.put(httpRequestHeaderKey, httpRequestHeaderValue);
    }

    public int getSize() {
        return headers.size();
    }

    public int getContentLength() {
        String contentLengthStr = headers.get(HttpRequestHeader.Content_Length.toString());
        if (contentLengthStr == null) {
            return 0;
        }
        return Integer.parseInt(contentLengthStr);
    }

    public String getCookie() {
        return headers.get(HttpRequestHeader.Cookie.toString());
    }
}
