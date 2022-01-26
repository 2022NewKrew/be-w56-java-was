package webserver.dispatcher.sta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton
 */
public class ResourceCache {

    private static final ResourceCache INSTANCE = new ResourceCache();

    public static ResourceCache getInstance() {
        return INSTANCE;
    }

    private ResourceCache() {
    }

    private Map<String, byte[]> urlAndFile = new HashMap<>();

    public byte[] addFile(String url, byte[] fileToBytes) {
        synchronized (urlAndFile) {
            urlAndFile.put(url, fileToBytes);
            return fileToBytes;
        }
    }

    public Optional<byte[]> getFile(String url) {
        synchronized (urlAndFile) {
            return Optional.ofNullable(urlAndFile.get(url));
        }
    }

    public void removeFile(String url) {
        synchronized (urlAndFile) {
            urlAndFile.remove(url);
        }
    }
}
