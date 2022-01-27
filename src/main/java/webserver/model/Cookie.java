package webserver.model;

public class Cookie {
    private final static String DEFAULT_PATH = "/";
    private final static long DEFAULT_MAX_AGE = 90 * 2400 * 60 * 60; // 90일

    private final String name;
    private final String value;
    private final String path;
    private final Long maxAge;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
        this.path = DEFAULT_PATH;
        this.maxAge = DEFAULT_MAX_AGE;
    }

    public String getHttpHeader() {
        return String.format("%s=%s; Max-Age=%d; Path=%s", name, value, maxAge, path);
    }

}
