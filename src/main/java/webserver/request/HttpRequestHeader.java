package webserver.request;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
public class HttpRequestHeader {
    private String host;
    private String userAgent;
    private String accept;
    private String connection;
    private String contentType;
    private Long contentLength;

    public void setHost(String host) {
        this.host = host;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }
}
