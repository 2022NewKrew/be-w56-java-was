package http.render;

import http.HttpResponse;
import http.StatusCode;
import webserver.exception.InternalServerErrorException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class HttpResponseRenderer {

    private static final String CRLF = "\r\n";

    private final List<ResponseBodyRenderer> bodyRenderers;

    public HttpResponseRenderer(List<ResponseBodyRenderer> bodyRenderers) {
        this.bodyRenderers = bodyRenderers;
    }

    public ByteArrayOutputStream render(HttpResponse response) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        Object body = response.getResponseBody();

        ByteArrayOutputStream bodyOutputStream = null;
        for(ResponseBodyRenderer bodyRenderer : bodyRenderers) {
            if(bodyRenderer.supports(body)) {
                bodyOutputStream = bodyRenderer.render(body);
                response.addHeader("Content-Length", String.valueOf(bodyOutputStream.size()));
                break;
            }
        }

        if(bodyOutputStream == null) {
            throw new InternalServerErrorException("응답을 만드는 적절한 객체가 없습니다.");
        }

        printResponseLine(ps, response);
        printHeader(ps, response);
        ps.print(CRLF);
        printBody(ps, bodyOutputStream);
        ps.flush();
        return byteArrayOutputStream;
    }

    private void printResponseLine(PrintStream ps, HttpResponse httpResponse) {
        StatusCode statusCode = httpResponse.getStatusCode();
        ps.print("HTTP/1.1 " + statusCode.getStatusCode() + " " + statusCode.name() + CRLF);
    }

    private void printHeader(PrintStream ps, HttpResponse httpResponse) {
        httpResponse.getHeaders().getAllHeaders().forEach((key, value) -> ps.print(key + ": " + value + CRLF));
    }

    private void printBody(PrintStream ps, ByteArrayOutputStream bodyStream) {
        byte[] bodyBytes = bodyStream.toByteArray();
        ps.write(bodyBytes, 0, bodyBytes.length);
    }
}
