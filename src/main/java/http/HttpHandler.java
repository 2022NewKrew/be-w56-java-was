package http;

public interface HttpHandler {
    boolean supports(HttpRequest httpRequest);
    HttpResponse handle(HttpRequest httpRequest) throws Throwable;
}
