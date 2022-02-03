package webserver.http;

public class HttpCookie {

    private final String name;
    private final String value;
    private String path;

    public HttpCookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (path == null) {
            return name + "=" + value;
        }
        return name + "=" + value + ";Path=" + path;
    }
}
