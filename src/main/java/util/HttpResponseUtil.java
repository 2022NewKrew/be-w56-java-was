package util;

import java.util.List;
import java.util.StringJoiner;
import webserver.http.HttpResponse;

public class HttpResponseUtil {

    public static String write(HttpResponse response) {
        List<String> headerLines = response.getHeader().getLines();
        StringJoiner joiner = new StringJoiner("\r\n", "", "\r\n");
        headerLines.forEach(joiner::add);
        return joiner + response.getBodyString();
    }

}
