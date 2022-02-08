package webserver.response.maker;

import webserver.http.HttpStatus;

public class HeaderLineMaker {

    private static final String NEXT_LINE = "\r\n";

    public static String make(int statusCode, String httpVersion) {
        HttpStatus httpStatus = HttpStatus.match(statusCode);
        return httpVersion+" "+httpStatus.getDescription()+" "+NEXT_LINE;
    }
}
