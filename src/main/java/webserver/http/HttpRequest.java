package webserver.http;

public class HttpRequest {
    private final HttpMethod method;
    private final String url;

    public HttpRequest(HttpMethod method, String url){
        this.method = method;
        this.url = url;
    }

    public HttpMethod getMethod(){
        return this.method;
    }

    public String getUrl(){
        return this.url;
    }

}

