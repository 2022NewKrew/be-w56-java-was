package webserver.http.request;

public class StringBody implements RequestBody<String> {

    private String requestBody;

    public StringBody(String body) {
        this.requestBody = body;
    }

    @Override
    public String getBody() {
        return requestBody;
    }
}
