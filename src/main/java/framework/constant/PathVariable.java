package framework.constant;

public enum PathVariable {
    BASEURL("http://localhost:8080"),
    STATIC_RESOURCE_BASE_URL("./webapp/");

    private final String path;

    PathVariable(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
