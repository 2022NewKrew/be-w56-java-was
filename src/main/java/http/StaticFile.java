package http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticFile implements Resource{

    private String type;
    private File file;

    public StaticFile(String type, File file) {
        this.type = type;
        this.file = file;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public byte[] getContent() {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
