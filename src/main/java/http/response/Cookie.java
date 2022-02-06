package http.response;

public class Cookie {
    private final String key;
    private final String value;
    private final String path;
    private final int maxAge;

    private Cookie(String key, String value, String path, int maxAge) {
        this.key = key;
        this.value = value;
        this.path = path;
        this.maxAge = maxAge;
    }

    public String toHeaderString() {
        return String.format("%s=%s; Path=%s; Max-Age=%d", key, value, path, maxAge);
    }

    public static class Builder {
        private String key;
        private String value;
        private String path;
        private int maxAge;

        private Builder(String key, String value) {
            this.key = key;
            this.value = value;
            this.path = "/";
            this.maxAge = 60 * 60 * 1000;
        }

        public static Builder of(String key, String value) {
            return new Builder(key, value);
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder maxAge(int milliseconds) {
            this.maxAge = milliseconds;
            return this;
        }

        public Cookie build() {
            return new Cookie(key, value, path, maxAge);
        }
    }
}
