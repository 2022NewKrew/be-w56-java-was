package util;

public class Cookie {
    private String name;
    private String value;
    private String path = "/";

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + "=" + value + "; " +
                "Path=" + path + ";";
    }
}
