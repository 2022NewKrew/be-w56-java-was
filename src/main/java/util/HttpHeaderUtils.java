package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpHeaderUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpHeaderUtils.class);

    public static String getHttpRequestUrl(String request) throws IllegalArgumentException {
        String[] token = request.split(" ");
        if(token.length < 3) {
            throw new IllegalArgumentException("request = " + request);
        }
        return token[1];
    }
}
