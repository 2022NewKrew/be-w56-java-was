package util;

import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpResponse;

public class HttpResponseUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponseUtil.class);

    public static String write(HttpResponse response) {
        StringJoiner joiner = new StringJoiner(" ", "", "\r\n");
        joiner.add(response.getVersion().toString());
        joiner.add(response.getStatus().getStatusMessage());
        joiner.add(response.getStatus().getStatusNumber());

        String requestLine = joiner.toString();
        String bodyContents = new String(response.getBody());

        return requestLine + response.headers().toHeaderString() + bodyContents;
    }

}
