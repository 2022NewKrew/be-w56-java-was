package webserver.http.response;

import lombok.Getter;
import webserver.http.response.body.ResponseBody;
import webserver.http.response.header.ResponseHeader;
import webserver.http.response.header.Status;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Getter
public class HttpResponse {

    private final ResponseHeader responseHeader;
    private final ResponseBody responseBody;

    private HttpResponse(ResponseHeader responseHeader, ResponseBody responseBody) {
        this.responseHeader = responseHeader;
        this.responseBody = responseBody;
    }

    public static HttpResponse of(String url, Integer statusCode) throws IOException {
        ResponseBody responseBody = ResponseBody.from(url);
        int bodyContentLength = responseBody.getBodyContentLength();
        ResponseHeader responseHeader = ResponseHeader.of(url, statusCode, bodyContentLength);
        return new HttpResponse(responseHeader, responseBody);
    }

    public static HttpResponse from(String message) {
        byte[] body = message.getBytes();
        ResponseBody responseBody = new ResponseBody(body);
        ResponseHeader responseHeader = ResponseHeader.builder()
                .status(Status.RESPONSE200)
                .url("/user/create/error")
                .contentType("text/html")
                .bodyContentLength(responseBody.getBodyContentLength())
                .build();
        return new HttpResponse(responseHeader, responseBody);
    }

    public void flush(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        responseHeader.write(dos);
        responseBody.write(dos);
        dos.flush();
    }
}
