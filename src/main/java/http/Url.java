package http;

public class Url {

    private static final String QUESTION_MARK = "\\?";
    private final HttpMethod method;
    private final String path;

    public Url(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getPath() {
        return path;
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

        String splitPath = path.split(QUESTION_MARK)[0];
        return splitPath.equals(url.path.split(QUESTION_MARK)[0]);
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        String splitPath = path.split("\\?")[0];
        result = 31 * result + splitPath.hashCode();
        return result;
    }
}
