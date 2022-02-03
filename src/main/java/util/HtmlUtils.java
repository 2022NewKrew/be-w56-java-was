package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HtmlUtils {

    public static void writeFile(String path, String body) {
        File file = new File(path);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(body);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
