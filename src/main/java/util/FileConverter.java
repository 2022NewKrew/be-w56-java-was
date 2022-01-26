package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileConverter {

    private FileConverter() {
    }

    public static byte[] fileToString(String uri) throws IOException {
        return Files.readAllBytes(
            new File("webapp" + uri).toPath());
    }
}
