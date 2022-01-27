package framework.http;

import framework.exception.InvalidRequestLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestLine {
    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
    private static final String REQUEST_LINE_SPLIT_DELIMITER = " ";
    private static final String PATH_QUERY_STRING_SPLIT_DELIMITER = "\\?";

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

        if (hasQueryString(split)) {
            this.queryString = new QueryString(split[1]);
        }
    }

    private boolean hasQueryString(String[] pathAndQuerySplit) {
        if (pathAndQuerySplit.length != 2) {
            return false;
        }

        return true;
    }

    private String[] parseRequestLineString(String requestLineString) {
        String[] split = requestLineString.split(REQUEST_LINE_SPLIT_DELIMITER);
        validateRequestLine(split);
        return split;
    }

    private void validateRequestLine(String[] requestLineSplit) {
        if (requestLineSplit.length != 3) {
            throw new InvalidRequestLineException();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString(String key) {
        return queryString.getValue(key);
    }
}
