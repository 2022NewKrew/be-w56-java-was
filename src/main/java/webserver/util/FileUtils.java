package webserver.util;

import java.io.File;

public class FileUtils {

    public static boolean hasExtension(String path) {
        String[] split = path.split("\\.");
        return split.length > 1;
    }

    public static String parseExtension(File file) {
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex + 1);
    }
}
