package webserver.model;

public class Cookie {
    private final static String DEFAULT_PATH = "/";
    private final static long DEFAULT_MAX_AGE = 90 * 2400 * 60 * 60;

    private final String name;
    private final String value;
    private final String path;
    private final Long maxAge;

    public Cookie(String name, String value, String path, long maxAge) {
        this.name = name;
        this.value = value;
        this.path = path;
        this.maxAge = maxAge;
    }

    public Cookie(String name, String value) {
        this(name, value, DEFAULT_PATH, DEFAULT_MAX_AGE);
    }

    public String getHttpHeader() {
        return String.format("%s=%s; Max-Age=%d; Path=%s", name, value, maxAge, path);
    }

}
