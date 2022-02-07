package view;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;

public class View {
    public static byte[] get(String filePath) throws IOException {
        return Files.toByteArray(new File("./webapp" + filePath));
    }
}
