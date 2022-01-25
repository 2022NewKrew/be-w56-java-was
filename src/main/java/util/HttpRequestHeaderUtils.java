package util;

import webserver.model.HttpRequestHeader;

public class HttpRequestHeaderUtils {
    public static HttpRequestHeader parseRequestHeader(String request) {
        String[] tokens = request.split(" ");
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(tokens[0], tokens[1], tokens[2]);
        return httpRequestHeader;
    }
}
