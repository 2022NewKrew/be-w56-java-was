package util;

public enum PathVariable {
    BASEURL("./webapp");

    private final String path;

    PathVariable(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
