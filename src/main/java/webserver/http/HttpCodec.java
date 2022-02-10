package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.connection.ConnectionIO;
import webserver.http.message.*;
import webserver.http.message.values.HttpHeaderNames;
import webserver.http.util.HttpRequestDecodeUtil;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

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
        final byte[] bytes = socket.readAllBytes();
        final String requestMessage = new String(bytes, StandardCharsets.UTF_8);
        log.debug("request : {}", requestMessage);
        System.out.println(requestMessage);
        final List<String> requestMsg = List.of(requestMessage.split(NEWLINE));
        final Iterator<String> it = requestMsg.iterator();

        final HttpStartLine startLine = getHttpStartLine(it);
        //TODO:queryString 분리작업 추가
        final HttpHeader header = getHttpHeader(it);
        final HttpBody body = getHttpBody(it);

        return  new HttpRequest(startLine, header, body);
    }

    private HttpStartLine getHttpStartLine(Iterator<String> it) {
        final String line = nextLine(it);
        final HttpStartLine startLine = HttpRequestDecodeUtil.parseStartLine(line);
        log.debug("request method : {}", startLine);
        return startLine;
    }

    private String nextLine(Iterator<String> it) {
        if(it.hasNext()) {
            return it.next();
        }
        return null;
    }

    private HttpHeader getHttpHeader(Iterator<String> it) {
        String accept = null;
        String cookie = null;
        int contentLength = 0;
        String contentType = null;

        while(it.hasNext()) {
            final String line = it.next();
            if(line.equals("")) {
                break;
            }

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
        }

        return new HttpHeader(accept, cookie, contentLength, contentType);
    }

    private HttpBody getHttpBody(Iterator<String> it) {

        return new HttpBody();
    }

    public void encode(HttpResponse response) {
        StringBuilder sb = new StringBuilder();
        HttpRequest request = response.getRequest();
        HttpHeader header = request.getHeader();
        sb.append(request.getVersion()).append(SPACE)
                .append(response.getStatus().getStatusCode()).append(SPACE)
                .append(response.getStatus().getReasonPhrase()).append(SPACE)
                .append(NEWLINE);

        sb.append(HttpHeaderNames.CONTENT_TYPE)
                .append(COLON).append(SPACE)
                .append(response.getContentType().getValue())
                .append(NEWLINE);

        sb.append(HttpHeaderNames.CONTENT_LENGTH)
                .append(COLON).append(SPACE)
                .append(response.getResponseBody().length)
                .append(NEWLINE);

        sb.append(NEWLINE);

        sb.append(new String(response.getResponseBody()));
        socket.writeAllBytes(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
