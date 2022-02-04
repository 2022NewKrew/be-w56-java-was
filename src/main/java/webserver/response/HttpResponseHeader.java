package webserver.response;

import mapper.ResponseSendDataModel;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpResponseHeader {
    private final byte[] header;

    private HttpResponseHeader(byte[] header) {
        this.header = header;
    }

    public static HttpResponseHeader makeHttpResponseHeader(ResponseSendDataModel model, int bodySize){
        String fileName = model.getName();

        String cookie = model.makeCookieString();

        if(fileName.matches("redirect:.*"))
            return new HttpResponseHeader(response302Header(fileName.substring(9), cookie));

        return new HttpResponseHeader(response200Header(bodySize, cookie));
    }

    private static byte[] response200Header(int lengthOfBodyContent, String cookie) {
        StringBuilder header = new StringBuilder();

        header.append("HTTP/1.1 200 OK \r\n");
        header.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        if(!"".equals(cookie))
            header.append(cookie + "\r\n");
        header.append("\r\n");

        return new String(header).getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] response302Header(String url, String cookie) {
        StringBuilder header = new StringBuilder();

        header.append("HTTP/1.1 302 \r\n");
        header.append("Location: " + url + "\r\n");

        if(!"".equals(cookie))
            header.append(cookie + "\r\n");
        header.append("\r\n");

        return new String(header).getBytes(StandardCharsets.UTF_8);
    }

    public byte[] getHeader() {
        return header;
    }
}
