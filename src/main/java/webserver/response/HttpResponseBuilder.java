package webserver.response;

import java.nio.charset.StandardCharsets;

public class HttpResponseBuilder {

    public static byte[] responseHeader(HttpResponse res) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + res.getStatusCode() +" OK \r\n");
        sb.append("Content-Length: " + res.getBody().length + "\r\n");
        res.getHeaders().entrySet().stream().forEach(e -> sb.append(e.getKey() + ": " + e.getValue() + "\r\n"));
        sb.append("\r\n");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static String get200Header() {
        return "";
    }

    private static String get404Header() {
        return "";
    }
}
