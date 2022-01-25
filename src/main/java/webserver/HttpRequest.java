package webserver;

public class HttpRequest {
    private final String method;
    private final String url;
    private final String protocol;

    public HttpRequest(String[] firstLineSplit){
        this.method = firstLineSplit[0];
        this.url = firstLineSplit[1];
        this.protocol = firstLineSplit[2];
    }
}
