package http;

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
        map.put("Content-Type", "text/html;charset=utf-8");
        map.put("Content-Length", Integer.toString(bytes.length));
        return Headers.create(map);
    }

    public Headers createResponseHeader(String url, Cookie cookie) {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Length", Integer.toString(bytes.length));
        map.put("Location", url);
        map.put("Set-Cookie", cookie.createCookieHeader());
        return Headers.create(map);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean isEmpty() {
        return bytes.length == 0;
    }
}
