package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static webserver.WebServer.DEFAULT_RESOURCES_DIR;

public class StaticResourceManager {

    private static final Map<String, ByteArray> resourceCacheMap = new HashMap<>();

    public static byte[] getBytesFromPath(String path) throws IOException {
        if (resourceCacheMap.containsKey(path)) {
            return resourceCacheMap.get(path).value;
        }
        byte[] body = Files.readAllBytes(new File(DEFAULT_RESOURCES_DIR + path).toPath());
        resourceCacheMap.put(path, new ByteArray(body));
        return body;
    }

    private static class ByteArray {
        private final byte[] value;

        private ByteArray(byte[] value) {
            this.value = value;
        }
    }
}
