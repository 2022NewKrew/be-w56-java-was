package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.connection.ConnectionIO;
import webserver.http.message.HttpHeader;
import webserver.http.message.HttpRequest;
import webserver.http.message.HttpResponse;
import webserver.http.message.HttpStartLine;
import webserver.http.message.values.HttpContentType;
import webserver.http.message.values.HttpHeaderNames;
import webserver.http.util.HttpRequestDecodeUtil;

import java.util.Map;

import static com.google.common.net.HttpHeaders.*;

public class HttpCodec {
    private static final Logger log = LoggerFactory.getLogger(HttpCodec.class);
    private static final String NEWLINE = "\r\n";
    private static final String SPACE = " ";
    private static final String COLON = ":";

    private final ConnectionIO socket;

    public HttpCodec(ConnectionIO socket) {
        this.socket = socket;
    }

    public HttpRequest decode() {
        final HttpStartLine startLine = getHttpStartLine();
        log.debug("request start line : {}", startLine);
        final Map<String, String> queryString = getQueryString(startLine);
        final HttpHeader header = getHttpHeader();
        final String body = getHttpBody(header.getContentLength());
        final Map<String, String> requestParams = getRequestParams(header, body);

        return  new HttpRequest(startLine, queryString, header, body, requestParams);
    }

    private HttpStartLine getHttpStartLine() {
        final String line = socket.readLine();
        final HttpStartLine startLine = HttpRequestDecodeUtil.parseStartLine(line);
        log.debug("request method : {}", startLine);
        return startLine;
    }

    private Map<String, String> getQueryString(HttpStartLine startLine) {
        final String queryString = startLine.getQueryStrings();
        if(queryString == null) {
            return null;
        }
        return HttpRequestDecodeUtil.parseQueryString(queryString);
    }

    private HttpHeader getHttpHeader() {
        String accept = null;
        String cookie = null;
        int contentLength = 0;
        String contentType = null;

        String line = socket.readLine();
        while(!line.equals("")) {
            final String[] headerLine = HttpRequestDecodeUtil.parseHeaderNames(line);

            switch (headerLine[0]) {
                case ACCEPT:
                    accept = headerLine[1];
                    break;
                case COOKIE:
                    cookie = headerLine[1];
                    break;
                case CONTENT_LENGTH:
                    contentLength = Integer.parseInt(headerLine[1]);
                    break;
                case CONTENT_TYPE:
                    contentType = headerLine[1];
                    break;
            }

            line = socket.readLine();
        }

        return new HttpHeader(accept, cookie, contentLength, contentType);
    }

    private String getHttpBody(int contentLength) {
        char[] buffer = new char[contentLength];
        socket.read(buffer, 0, contentLength);

        return new String(buffer);
    }

    private Map<String, String> getRequestParams(HttpHeader header, String body) {
        if(isApplicationXWwwFormUrlencoded(header.getContentType())){
            return HttpRequestDecodeUtil.parseQueryString(body);
        }
        return null;
    }

    private boolean isApplicationXWwwFormUrlencoded(String contentType) {
        return contentType != null &&
                contentType.equals(HttpContentType.APPLICATION_X_WWW_FORM_URLENCODED.getValue());
    }

    public void encode(HttpResponse response) {
        StringBuilder sb = new StringBuilder();
        HttpRequest request = response.getRequest();

        sb.append(request.getVersion()).append(SPACE)
                .append(response.getStatus().getStatusCode()).append(SPACE)
                .append(response.getStatus().getReasonPhrase()).append(SPACE)
                .append(NEWLINE);

        sb.append(HttpHeaderNames.CONTENT_TYPE.getName())
                .append(COLON).append(SPACE)
                .append(response.getContentType().getValue())
                .append(NEWLINE);

        sb.append(HttpHeaderNames.CONTENT_LENGTH.getName())
                .append(COLON).append(SPACE)
                .append(response.getResponseBody().length)
                .append(NEWLINE);

        sb.append(NEWLINE);
        socket.writeBytes(sb.toString());

        socket.write(response.getResponseBody(), 0, response.getResponseBody().length);
        socket.writeBytes(NEWLINE);
        socket.flush();
    }
}
