package util;

import model.RequestHeader;

public class HttpRequestHeaderUtils {
    public static void setRequest(RequestHeader requestHeader, String request) {
        String[] tokens = request.split(" ");

        requestHeader.setMethod(tokens[0]);
        requestHeader.setUri(tokens[1]);
        requestHeader.setProtocol(tokens[2]);
    }

    public static void setHeader(RequestHeader requestHeader, String header) {
        String[] tokens = header.split(": ");
        requestHeader.putHeader(tokens[0], tokens[1]);
    }
}
