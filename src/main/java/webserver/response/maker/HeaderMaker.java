package webserver.response.maker;

import util.HttpRequestUtils;
import webserver.http.ContentType;
import webserver.http.Response;

public class HeaderMaker {

    private static final String NEXT_LINE = "\r\n";

    public static String make(Response response, String httpVersion, int lengthOfContent) {
        ContentType contentType = HttpRequestUtils.parseExtension(response.getPath());
        String headerLine = HeaderLineMaker.make(response.getStatusCode(), httpVersion);
        int statusCode = response.getStatusCode();
        if (statusCode == 302) {
            return headerLine+locationHeader(response.getPath())+cookieHeader(response);
        }
        if (statusCode == 200) {
            return headerLine+contentTypeHeader(contentType, lengthOfContent)+cookieHeader(response);
        }
        return headerLine+cookieHeader(response);
    }

    private static String cookieHeader(Response response) {
        return response.getCookie()+NEXT_LINE;
    }

    private static String locationHeader(String location) {
        return "Location: " + location + NEXT_LINE;
    }

    private static String contentTypeHeader(ContentType contentType, int lengthOfContent) {
        String type = "Content-Type: "+contentType.getType()+";charset=utf-8"+NEXT_LINE;
        String length = "Content-Length: " + lengthOfContent + NEXT_LINE;
        return type+length;
    }
}
