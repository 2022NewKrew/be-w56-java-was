package infrastructure.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static infrastructure.config.ServerConfig.DEFAULT_RESOURCE_PATH;

public class HttpByteArrayBody implements HttpBody {

    private final byte[] value;

    public HttpByteArrayBody(byte[] value) {
        this.value = value;
    }

    public static HttpBody setFile(String file) throws IOException {
        return new HttpByteArrayBody(Files.readAllBytes(new File(DEFAULT_RESOURCE_PATH + file).toPath()));
    }

    @Override
    public byte[] toByteStream() {
        return value;
    }

    @Override
    public int length() {
        return value.length;
    }
}
