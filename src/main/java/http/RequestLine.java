package http;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMethod;

@Getter
public class RequestLine {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final HttpMethod httpMethod;

    private final String url;

    private final String queryString;

    public RequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        this.httpMethod = HttpMethod.valueOf(tokens[0]);

        String[] url = tokens[1].split("\\?");
        this.url = url[0];
        this.queryString = (url.length == 2) ? url[1] : "";

        log.debug("url : {}", this.url);
        log.debug("queryString : {}", this.queryString);
    }
}
