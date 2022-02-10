package http.view;

import http.cookie.Cookie;
import http.response.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class OutputView {
    public static void sendResponse(HttpResponse response, DataOutputStream dos) throws IOException {
        dos.writeBytes(String.format("%s %s\r\n", response.getProtocolVersion().getValue(), response.getStatus()));
        for (Map.Entry<String, String> header: response.getHeaders()) {
            dos.writeBytes(String.format("%s: %s\r\n", header.getKey(), header.getValue()));
        }
        for (Cookie cookie: response.getCookies()) {
            dos.writeBytes(String.format("Set-Cookie: %s\r\n", cookie));
        }

        byte[] body = response.getBody();
        if (body.length > 0) {
            dos.writeBytes(String.format("Content-Length: %d\r\n", body.length));
        }

        dos.writeBytes("\r\n");

        if (body.length > 0) {
            dos.write(body, 0, body.length);
        }

        dos.flush();
    }
}
