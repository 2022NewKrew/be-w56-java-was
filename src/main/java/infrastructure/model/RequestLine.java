package infrastructure.model;

import java.util.Objects;

public class RequestLine {

    private final RequestMethod requestMethod;
    private final Path path;

    public RequestLine(RequestMethod requestMethod, Path path) {
        this.requestMethod = requestMethod;
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestLine that = (RequestLine) o;
        return requestMethod == that.requestMethod && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestMethod, path);
    }

    @Override
    public String toString() {
        return "RequestLine{" +
                "requestMethod=" + requestMethod +
                ", path=" + path +
                '}';
    }
}
