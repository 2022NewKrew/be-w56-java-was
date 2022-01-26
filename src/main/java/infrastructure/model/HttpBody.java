package infrastructure.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static infrastructure.config.ServerConfig.DEFAULT_RESOURCE_PATH;

public class HttpBody {

    private final byte[] value;

    public HttpBody(byte[] value) {
        this.value = value;
    }

    public static HttpBody valueOfFile(String file) throws IOException {
        return new HttpBody(
                Files.readAllBytes(new File(DEFAULT_RESOURCE_PATH + file).toPath()));
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpBody httpBody = (HttpBody) o;
        return Arrays.equals(value, httpBody.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
