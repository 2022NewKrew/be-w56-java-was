package http.request;

import exception.InvalidPathException;

public class URI {

    private static final String EXTENSION_DELIMITER = ".";

    private final String path;
    private final Queries queries;

    public URI(String path, Queries queries) {
        validatePath(path);
        this.path = path;
        this.queries = queries;
    }

    private void validatePath(String path) {
        if (path == null) {
            throw new InvalidPathException("null");
        }
    }

    public String getFileExtension() {
        int extensionIdx = path.lastIndexOf(EXTENSION_DELIMITER);

        return path.substring(extensionIdx + 1);
    }

    public String getPath() {
        return path;
    }

    public Queries getQueries() {
        return queries;
    }
}
