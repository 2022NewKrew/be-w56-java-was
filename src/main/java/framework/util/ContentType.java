package framework.util;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

public class ContentType {
    private static final Tika tika = new Tika();
    private static final ContentType instance = new ContentType();

    private ContentType(){}

    public static ContentType getInstance() {
        return instance;
    }

    public String getContentType(File file) throws IOException {
        return tika.detect(file);
    }
}
