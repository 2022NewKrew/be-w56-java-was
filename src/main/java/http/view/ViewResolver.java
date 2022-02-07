package http.view;

import http.exception.NotFound;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    private static final String STATIC_FILE_PATH = "./webapp";

    public static byte[] getView(String path) {
        return getFile(STATIC_FILE_PATH + path);
    }

    // ----------------------------------------------------------------------------------------------------------

    private static byte[] getFile(String path) {
        try {
            return Files.readAllBytes(new File(path).toPath());
        } catch (IOException e) {
            throw new NotFound();
        }
    }
}
