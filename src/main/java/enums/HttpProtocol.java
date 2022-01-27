package enums;

public enum HttpProtocol {
    HTTP_1_0("HTTP/1.0"), HTTP_1_1("HTTP/1.1"), HTTP_2_0("HTTP/2.0"), HTTPS("HTTPS");

    private final String name;

    HttpProtocol(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
