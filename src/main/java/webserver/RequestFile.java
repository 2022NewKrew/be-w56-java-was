package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RequestFile {
    File file;

    private static final String ROOT_DIRECTORY = "./webapp";

    public RequestFile(String filePath) {
        this.file = new File(ROOT_DIRECTORY + filePath);
    }

    public byte[] getFileBytes () throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
