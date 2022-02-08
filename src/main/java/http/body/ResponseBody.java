package http.body;

import http.header.Cookie;
import http.header.Headers;
import http.header.MimeType;

import java.util.HashMap;
import java.util.Map;

public class ResponseBody {
    private final byte[] bytes;

    public ResponseBody() {
        this(new byte[]{});
    }

    public ResponseBody(byte[] bytes) {
        validateNull(bytes);
        this.bytes = bytes;
    }

    private void validateNull(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException();
        }
    }

    public Headers createResponseHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Length", Integer.toString(bytes.length));
        return Headers.create(map);
    }

    public Headers createResponseHeader(MimeType mimeType, Cookie cookie) {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Length", Integer.toString(bytes.length));
        map.put("Content-Type", mimeType.getMimeType());
        if (cookie != null) {
            map.put("Set-Cookie", cookie.createHeader());
        }
        return Headers.create(map);
    }

    public Headers createResponseHeader(String url, Cookie cookie) {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Length", Integer.toString(bytes.length));
        map.put("Location", url);
        if (cookie != null) {
            map.put("Set-Cookie", cookie.createHeader());
        }
        return Headers.create(map);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean isEmpty() {
        return bytes.length == 0;
    }
}
