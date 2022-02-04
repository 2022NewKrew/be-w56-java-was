package http.request;

import exception.InvalidPathException;

public class URI {

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

    public String getPath() {
        return path;
    }

    public Queries getQueries() {
        return queries;
    }
}
