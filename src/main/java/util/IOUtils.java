package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class IOUtils {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * @param br
     *            Request Body 를 시작하는 시점이어야
     * @param contentLength
     *            Request Header 의 Content-Length 값이다.
     * @throws IOException : IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        int result = br.read(body, 0, contentLength);
        if (result == -1) {
            return null;
        }
        String bodyString = URLDecoder.decode(String.copyValueOf(body), StandardCharsets.UTF_8);
        log.debug("request body : {}",bodyString);
        return bodyString;
    }
}
