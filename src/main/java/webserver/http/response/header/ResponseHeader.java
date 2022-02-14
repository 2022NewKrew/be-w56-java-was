package webserver.http.response.header;

import lombok.Builder;
import org.apache.tika.Tika;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class ResponseHeader {
    private final Status status;
    private final int bodyContentLength;
    private final String url;
    private final String contentType;

    @Builder
    private ResponseHeader(Status status, int bodyContentLength, String url, String contentType) {
        this.status = status;
        this.bodyContentLength = bodyContentLength;
        this.url = url;
        this.contentType = contentType;
    }

    public static ResponseHeader of(String url, Integer statusCode, int bodyContentLength)
            throws IOException {

        Status status = Status.RESPONSE200;

        if (statusCode == 302) {
            status = Status.RESPONSE302;
        }

        return ResponseHeader.builder()
                .status(status)
                .bodyContentLength(bodyContentLength)
                .url(url)
                .contentType(getContentTypeByUrl(url))
                .build();
    }

    private static String getContentTypeByUrl(String url) throws IOException {
        return new Tika().detect(new File("./webapp" + url));
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.writeBytes(createHeaderMessage());
    }

    private String createHeaderMessage() {
        return "HTTP/1.1 " + status.getCode() + " " + status.getMessage() + "\r\n" +
                "Location : " + url + "\r\n" +
                "Content-Type: " + contentType + ";charset=utf-8\r\n" +
                "Content-Length: " + bodyContentLength + "\r\n" +
                "\r\n";
    }
}
