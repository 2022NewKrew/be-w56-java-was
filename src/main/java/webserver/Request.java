package webserver;

public class Request {

    private final String method;
    private final String target;
    private final String version;

    public static Request of(String requestLine) {
        String[] tokens = requestLine.split(" ");
        return new Request(tokens[0], tokens[1], tokens[2]);
    }

    private Request(String method, String target, String version) {
        this.method = method;
        this.target = target;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }
}
