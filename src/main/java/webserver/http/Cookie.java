package webserver.http;

public class Cookie {

    // Cookie
    private final String name;
    private String value;

    // Cookie attributes
    private String domain;
    private int maxAge = -1;
    private String path;
    private boolean secure;
    private boolean isHttpOnly;

    public Cookie(String name, String value) {
        if (name == null || name.isEmpty() || value == null || value.isEmpty()) {
            throw new IllegalArgumentException("에러: 적절한 쿠키 값이 아닙니다.");
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return isHttpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        isHttpOnly = httpOnly;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(name)
                .append("=")
                .append(value)
                .append("; Max-Age=")
                .append(maxAge);

        if (domain != null && !domain.isBlank()) {
            stringBuilder
                    .append("; Domain=")
                    .append(domain);
        }
        if (path != null && !path.isBlank()) {
            stringBuilder
                    .append("; Path=")
                    .append(path);
        }
        if (secure) {
            stringBuilder
                    .append("; Secure");
        }
        return stringBuilder.toString();
    }
}
