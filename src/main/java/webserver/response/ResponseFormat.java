package webserver.response;

public interface ResponseFormat {
    void sendResponse (ResponseCode status);
    void setCookie (String key, String value);
}
