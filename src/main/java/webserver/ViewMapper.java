package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewMapper {

    public static byte[] getBytes(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }
}
