package http;

import java.util.HashMap;
import java.util.Map;

public class ResponseBody {
    private final byte[] bytes;

    public ResponseBody() {
        bytes = new byte[]{};
    }

    public ResponseBody(byte[] bytes) {
        this.bytes = bytes;
    }

    public Headers createResponseHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "text/html;charset=utf-8");
        map.put("Content-Length", Integer.toString(bytes.length));
        return Headers.create(map);
    }

    public Headers createResponseHeader(String url) {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Length", Integer.toString(bytes.length));
        map.put("Location", url);
        return Headers.create(map);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean isEmpty() {
        return bytes.length == 0;
    }
}
