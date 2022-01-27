package was.util;

import di.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Bean
public class StaticResourceReader {
    public byte[] read(String path) throws IOException {
        final String RESOURCE_PATH = "src/main/resources";
        final File file = new File(RESOURCE_PATH + path);
        return Files.readAllBytes(file.toPath());
    }
}
