package util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;

public class SampleTest {

    @Test
    void testMimeType() throws IOException {
        String mimeType = Files.probeContentType(new File("./webapp/css/style.css").toPath());
        System.out.println("mimeType: " + mimeType);
    }
}
