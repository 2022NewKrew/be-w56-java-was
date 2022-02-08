package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class FilesUtils {
    private static final String BASIC_FILE_PATH = "./webapp";

    public static byte[] fileToByte(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(BASIC_FILE_PATH).append(url);

        return Files.readAllBytes(new File(sb.toString()).toPath());
    }
}
