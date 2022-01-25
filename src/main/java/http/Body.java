package http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Body {
    private final byte[] bytes;

    public Body(byte[] bytes) {
        this.bytes = bytes;
    }

    public static Body create(File file) throws IOException {
        return new Body(Files.readAllBytes(file.toPath()));
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
}
