package webserver.util;

import java.util.Map;
import java.util.StringJoiner;
import webserver.http.HttpResponse;

public class HttpResponseUtil {

    public static String writeResponseLine(HttpResponse response) {
        StringJoiner joiner = new StringJoiner(" ", "", "\r\n");
        joiner.add(response.getVersion().toString());
        joiner.add(response.getStatus().getStatusNumber());
        joiner.add(response.getStatus().getStatusMessage());
        return joiner.toString();
    }

    public static String writeHeader(HttpResponse response) {
        Map<String, String> headers = response.headers().getMap();
        if (headers.isEmpty()) {
            return "";
        }
        StringJoiner joiner = new StringJoiner("\r\n", "", "\r\n");
        headers.forEach((key, value) -> joiner.add(key + ": " + value));
        return joiner.toString();
    }

    public static String writeBody(HttpResponse response) {
        if (response.getBody().length == 0) {
            return "";
        }
        return new String(response.getBody());
    }

}
