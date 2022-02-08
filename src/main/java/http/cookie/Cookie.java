package http.cookie;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Cookie {
    private final String name;
    private final String value;

    private final String path;

    @Builder
    public Cookie(String name, String value, String path) {
        this.name = name;
        this.value = value;
        this.path = path;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(name)
                .append('=')
                .append(value)
                .append(';');

        if (path != null) {
            sb.append(" Path=")
                    .append(path);
        }
        return sb.toString();
    }
}
