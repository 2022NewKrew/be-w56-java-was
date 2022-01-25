package http;

public class Url {

    private final HttpMethod method;
    private final String path;

    public Url(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Url url = (Url) o;

        if (method != url.method) {
            return false;
        }
        return path.equals(url.path);
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
