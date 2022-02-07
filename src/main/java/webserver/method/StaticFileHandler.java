package webserver.method;

import java.io.File;
import java.nio.file.Path;

public class StaticFileHandler {
    private final File file;

    public StaticFileHandler(File file) {
        this.file = file;
    }

    public Path getPath() {
        return file.toPath();
    }
}
