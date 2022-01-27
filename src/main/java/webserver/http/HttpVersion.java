package webserver.http;

public class HttpVersion {

    private final String version;

    public HttpVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return version;
    }
}
