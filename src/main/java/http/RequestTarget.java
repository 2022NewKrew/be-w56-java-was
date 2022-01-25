package http;

import com.google.common.base.Strings;

import java.util.Objects;

public class RequestTarget {
    private final String path;

    public RequestTarget(String path) {
        validateNull(path);
        this.path = path;
    }

    private void validateNull(String path) {
        if (Strings.isNullOrEmpty(path)) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RequestTarget requestTarget = (RequestTarget) object;
        return Objects.equals(this.path, requestTarget.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    public String findPath() {
        String path = this.path;
        if (this.path.equals("/")) {
            path += "index.html";
        }
        return "./webapp" + path;
    }
}
