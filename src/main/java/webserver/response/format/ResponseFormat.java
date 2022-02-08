package webserver.response.format;

import webserver.response.ResponseCode;

public interface ResponseFormat {
    void sendResponse (ResponseCode status);
    void setCookie (String key, String value);
}
