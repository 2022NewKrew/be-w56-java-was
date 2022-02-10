package infrastructure.model;

import infrastructure.validation.PathValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Path {

    private final Map<String, String> variables;
    private final ContentType contentType;
    private final String value;

    public Path(ContentType contentType, String value) {
        this.contentType = contentType;
        this.value = value;
        this.variables = new HashMap<>();
    }

    public Path(Map<String, String> variables, ContentType contentType, String value) {
        this.variables = variables;
        this.contentType = contentType;
        this.value = value;
    }

    public boolean matchHandler(String handler) {
        return value.startsWith(handler);
    }

    public String getValue() {
        return value;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public boolean equals(String value) {
        return this.value.equals(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(variables, path.variables) && contentType == path.contentType && Objects.equals(value, path.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables, contentType, value);
    }

    @Override
    public String toString() {
        return "Path{" +
                "variables=" + variables +
                ", contentType=" + contentType +
                ", value='" + value + '\'' +
                '}';
    }

    public static class Builder {
        private Map<String, String> variables;
        private ContentType contentType;
        private String value;

        private Builder() {
        }

        public Builder variables(Map<String, String> variables) {
            this.variables = variables;
            return this;
        }

        public Builder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Path build() {
            PathValidator.assertContentType(contentType);
            if (variables == null) {
                return new Path(contentType, value);
            }
            return new Path(variables, contentType, value);
        }
    }


}
