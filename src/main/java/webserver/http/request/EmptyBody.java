package webserver.http.request;

public class EmptyBody implements RequestBody<Void> {

    @Override
    public Void getBody() {
        throw new RuntimeException("Empty Body");
    }
}
