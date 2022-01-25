package http;

import exception.InvalidRequestLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class RequestLine {
    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
    public static final String REQUEST_LINE_SPLIT_DELIMITER = " ";
    public static final String PATH_QUERY_STRING_SPLIT_DELIMITER = "\\?";

    private String method;
    private String path;
    private String protocol;
    private QueryString queryString;

    public RequestLine(BufferedReader bufferedReader) throws IOException {
        String requestLineString = bufferedReader.readLine();
        log.debug("request line : {}", requestLineString);
        String[] split = parseRequestLineString(requestLineString);
        this.method = split[0];
        this.protocol = split[2];
        makePathAndQueryString(split[1]);
    }

    private void makePathAndQueryString(String pathAndQueryString) {
        String[] split = pathAndQueryString.split(PATH_QUERY_STRING_SPLIT_DELIMITER);
        this.path = split[0];
        if (split.length >= 2) {
            this.queryString = new QueryString(split[1]);
        }
    }

    private String[] parseRequestLineString(String requestLineString) {
        String[] split = requestLineString.split(REQUEST_LINE_SPLIT_DELIMITER);
        validateRequestLine(split);
        return split;
    }

    private void validateRequestLine(String[] requestLineSplit) {
        if (requestLineSplit.length < 3) {
            throw new InvalidRequestLineException();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public QueryString getQueryString() {
        return queryString;
    }

    public Map<String, String> getQueryStringMap() {
        return queryString.getQueryStringMap();
    }
}
