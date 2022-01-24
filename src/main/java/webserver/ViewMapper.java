package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewMapper {

    private static final String PREFIX = "./webapp";

    public static byte[] getBytes(String url) throws IOException {
        return Files.readAllBytes(new File(PREFIX + url).toPath());
    }
}
