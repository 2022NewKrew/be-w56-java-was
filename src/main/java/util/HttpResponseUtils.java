package util;

import network.HttpRequest;
import network.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class HttpResponseUtils {

    private static List<String> setHeaders(HttpRequest httpRequest, HttpStatus httpStatus, byte[] body) {
        List<String> headers = new ArrayList<>();
        String statusLine = httpStatus.getStatusLine();
        String contentType = httpRequest.getContentType();
        int lengthOfBodyContent = body.length;

        headers.add(statusLine);
        headers.add("Content-Type: " + contentType + ";charset=utf-8\r\n");
        headers.add("Content-Length: " + lengthOfBodyContent + "\r\n");
        headers.add("\r\n");

        return headers;
    }

    public static List<String> response200(HttpRequest httpRequest, byte[] body) {
        return setHeaders(httpRequest, HttpStatus.OK, body);
    }

    public static List<String> response302(HttpRequest httpRequest, String location) {
        List<String> headers = setHeaders(httpRequest, HttpStatus.FOUND, new byte[0]);
        headers.add(1, "Location: http://localhost:8080" + location + "\r\n");
        return headers;
    }
}
