package util;

import webserver.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */

    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static HttpRequest printAllRequestHeadersAndReturnRequestLine(BufferedReader br) throws IOException {
        HttpRequest httpRequest = printAndReturnRequestLine(br);
        printRequestHeaders(br);
        return httpRequest;
    }

    private static HttpRequest printAndReturnRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.debug("request line: {}", line);
        return new HttpRequest(line);
    }

    private static void printRequestHeaders(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!line.equals("")) {
            line = br.readLine();
            if (line == null) { return; }
            log.debug("header: {}", line);
        }
    }
}
