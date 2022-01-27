package was.http;

import was.meta.HttpHeader;
import was.meta.HttpStatus;
import was.meta.HttpVersion;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpVersion version;
    private HttpStatus status;
    private Map<String, String> headers;
    private Cookie cookie;
    private byte[] body;

    public static HttpResponse of(HttpVersion version) {
        HttpResponse response = new HttpResponse();
        response.version = version;
        response.headers = new HashMap<>();
        response.cookie = new Cookie();
        return response;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void addCookie(String key, String value) {
        cookie.addAttribute(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    private String getHeader() {
        StringBuilder stringBuilder = new StringBuilder();
        headers.forEach((key, value) -> {
            stringBuilder.append(key).append(": ").append(value).append("\r\n");
        });
        return stringBuilder.toString();
    }

    private String getCookies() {
        StringBuilder stringBuilder = new StringBuilder(HttpHeader.SET_COOKIE + ": ");
        cookie.getAttributes().forEach((key, value) -> {
            stringBuilder.append(key).append("=").append(value).append("; ");
        });
        stringBuilder.append(Cookie.ATTRIBUTE_MAX_AGE).append("=").append(Cookie.MAX_AGE);
        return stringBuilder.toString();
    }

    public byte[] toBytes() {
        final byte[] header = new StringBuilder()
                .append(version.getValue()).append(" ")
                .append(status.getValue()).append("\r\n")
                .append(getHeader()).append(getCookies()).append("\r\n\r\n")
                .toString().getBytes(StandardCharsets.UTF_8);

        if (body != null) {
            final byte[] result = new byte[header.length + body.length];
            System.arraycopy(header, 0, result, 0, header.length);
            System.arraycopy(body, 0, result, header.length, body.length);
            return result;
        }

        final byte[] result = new byte[header.length];
        System.arraycopy(header, 0, result, 0, header.length);

        return result;
    }

//    @Override
//    public String toString() {
//        return new StringBuilder()
//                .append(version.getValue()).append(" ")
//                .append(status.getValue()).append("\r\n")
//                .append(getHeader()).append("\r\n\r\n")
//                .append(new String(body)).append("\r\n")
//                .toString();
//    }
}
