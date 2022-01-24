package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    private final static String REQUEST_LINE_REGEX = " ";
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    /**
     * @param BufferedReader는
     *            Request Line을 시작하는 시점이어야한다.
     * @return Request Line의 정보를 읽어 String[]를 전달한다. (Request Line의 정보가 들어있다.)
     *            (method, url, version)
     * @throws IOException
     */
    public static String[] readRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        String[] tokens = StringUtils.split(requestLine, REQUEST_LINE_REGEX);
        log.debug("Request Line : {}, {}, {}" , tokens[0], tokens[1], tokens[2]);

        return tokens;
    }

    /**
     * @param BufferedReader는
     *            Request Header를 시작하는 시점이어야한다.
     * @return Request Header의 정보를 읽어 List<HttpRequestUtils.Pair>를 반환한다.
     *            (key, value)
     * @throws IOException
     */
    public static List<HttpRequestUtils.Pair> readRequestHeaders(BufferedReader br) throws IOException {
        String line = "";
        List<HttpRequestUtils.Pair> requestHeaders = new ArrayList<>();

        while (!(line = br.readLine()).equals("")) {
            HttpRequestUtils.Pair header = HttpRequestUtils.parseHeader(line);

            log.debug("Request Header : {}", header);
            requestHeaders.add(header);
        }

        return requestHeaders;
    }
}
