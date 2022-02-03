package http.request;

import exception.InValidPathException;

public class Path {

    private static final String EXTENSION_DELIMITER = ".";

    private final String path;

    public Path(String path) {
        validate(path);
        this.path = path;
    }

    private void validate(String path) {
        if (path == null) {
            throw new InValidPathException("null");
        }
    }

    public String getFileExtension() {
        int extensionIdx = path.lastIndexOf(EXTENSION_DELIMITER);

        return path.substring(extensionIdx + 1);
    }

    public String getPath() {
        return path;
    }
}
