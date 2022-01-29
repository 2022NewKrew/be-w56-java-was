package http;

import java.util.HashMap;
import java.util.Map;

public class Body {
    private final byte[] bytes;

    public Body() {
        bytes = new byte[]{};
    }

    public Body(byte[] bytes) {
        this.bytes = bytes;
    }

    public Headers createResponseHeader() {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "text/html;charset=utf-8");
        map.put("Content-Length", Integer.toString(bytes.length));
        return Headers.create(map);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public boolean isEmpty() {
        return bytes.length == 0;
    }
}
