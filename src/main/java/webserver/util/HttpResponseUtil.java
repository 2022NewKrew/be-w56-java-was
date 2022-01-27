package webserver.util;

import java.util.StringJoiner;
import webserver.http.HttpResponse;

public class HttpResponseUtil {

    public static String write(HttpResponse response) {
        StringJoiner joiner = new StringJoiner(" ", "", "\r\n");
        joiner.add(response.getVersion().toString());
        joiner.add(response.getStatus().getStatusNumber());
        joiner.add(response.getStatus().getStatusMessage());
        String responseLine = joiner.toString();

        String bodyContents = "";
        if (response.getBody().length > 0) {
            bodyContents = new String(response.getBody());
        }

        return responseLine + response.headers().toHeaderString() + bodyContents;
    }

}
