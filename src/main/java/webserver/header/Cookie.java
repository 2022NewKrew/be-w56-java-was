package webserver.header;

public class Cookie {

    private final String name;
    private final String value;
    private String path;

    private Cookie (String name, String value) {
        this.name = name;
        this.value = value;
        this.path = "/";
    }

    public static Cookie create(String name, String value) {
        return new Cookie(name, value);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String toHeader() {
        return "Set-Cookie: "+toNameValue()+"; "+toPath();
    }

    private String toNameValue() {
        return name+"="+value;
    }

    private String toPath() {
        return "Path="+path;
    }
}
