package was.server.util;

import di.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Bean
public class StaticResourceReader {

    public byte[] read(String path) throws RuntimeException {
        final String resourcePath = "src/main/resources";

        try {
            return Files.readAllBytes(Path.of(resourcePath + path));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
