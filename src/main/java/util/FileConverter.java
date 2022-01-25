package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileConverter {

    private FileConverter() {
    }

    public static String fileToString(String uri) throws IOException {

        return Files.readString(
            new File("./webapp" + uri).toPath());
    }
}
