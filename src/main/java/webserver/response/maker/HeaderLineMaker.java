package webserver.response.maker;

import webserver.http.HttpStatus;

public class HeaderLineMaker {

    private static final String NEXT_LINE = "\r\n";

    public static String make(HttpStatus statusCode, String httpVersion) {
        return httpVersion + " " + statusCode.getDescription() + " " + NEXT_LINE;
    }
}
