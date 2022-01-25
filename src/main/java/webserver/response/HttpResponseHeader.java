package webserver.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class HttpResponseHeader {
    private String accessControlAllowOrigin;
    private String server;
    private String connection;
    private String contentType;
    private Long contentLength;
    private String Date;

    public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
        this.accessControlAllowOrigin = accessControlAllowOrigin;
    }

    public void setServer(String server) {
        this.server = server;
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

    public void setDate(String date) {
        Date = date;
    }
}
