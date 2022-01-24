package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileIOUtils {

    private static final String PATHNAME = "./webapp";

    private FileIOUtils() {
    }

    public static byte[] loadStaticFile(String path) throws IOException {
        File file = new File(PATHNAME + path);
        return Files.readAllBytes(file.toPath());
    }
}
