package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.connection.ConnectionIO;
import webserver.http.message.*;
import webserver.http.message.values.HttpHeaderNames;
import webserver.http.util.HttpRequestDecodeUtil;

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
        //TODO:queryString 분리작업 추가
        final HttpHeader header = getHttpHeader();
        final HttpBody body = getHttpBody();

        return  new HttpRequest(startLine, header, body);
    }

    private HttpStartLine getHttpStartLine() {
        final String line = socket.readLine();
        final HttpStartLine startLine = HttpRequestDecodeUtil.parseStartLine(line);
        log.debug("request method : {}", startLine);
        return startLine;
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

    private HttpBody getHttpBody() {

        return new HttpBody();
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
