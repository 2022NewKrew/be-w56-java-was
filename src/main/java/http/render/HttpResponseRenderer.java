package http.render;

import http.HttpResponse;
import http.StatusCode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HttpResponseRenderer {

    private static final String CRLF = "\r\n";

    public ByteArrayOutputStream render(HttpResponse response) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(byteArrayOutputStream);
        byte[] body = response.getResponseBody();

        printResponseLine(ps, response);
        printHeader(ps, response);
        ps.print(CRLF);
        printBody(ps, body);
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

    private void printBody(PrintStream ps, byte[] responseBody) {
        ps.write(responseBody, 0, responseBody.length);
    }
}
