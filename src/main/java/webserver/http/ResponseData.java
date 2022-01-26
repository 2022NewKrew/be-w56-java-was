package webserver.http;

public class ResponseData {
    private HttpStatusCode statusCode;
    private String url;

    public ResponseData(HttpStatusCode statusCode, String url){
        this.statusCode = statusCode;
        this.url = url;
    }

    public HttpStatusCode getStatusCode(){
        return statusCode;
    }

    public String getUrl(){
        return url;
    }
}
