package infrastructure.model;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Path {

    private final ContentType contentType;
    private String value;

    public Path(String value, ContentType contentType) {
        this.value = value;
        this.contentType = contentType;
    }

    public static Path create(String value) {
        ContentType contentType = ContentType.valueOfPath(value);
        return new Path(value, contentType);
    }

    public boolean matchHandler(String handler) {
        return value.startsWith(handler);
    }

    public void separatePathPrefix() {
        String[] split = value.split("/");

        this.value = IntStream.range(1, split.length)
                .mapToObj(i -> split[i])
                .collect(Collectors.joining(""));
    }

    public String getValue() {
        return value;
    }

    public ContentType getContentType() {
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Objects.equals(value, path.value) && contentType == path.contentType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, contentType);
    }

    @Override
    public String toString() {
        return "Path{" +
                "value='" + value + '\'' +
                ", contentType=" + contentType +
                '}';
    }

}
