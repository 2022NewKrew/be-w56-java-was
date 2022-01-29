package webserver.http.response;

public interface ResponseBody<T> {
    T getResponseValue();
}
