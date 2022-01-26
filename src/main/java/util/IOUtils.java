package util;

import http.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {

    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

    private IOUtils() {
    }

    public static String readRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        log.debug("request line: {}", requestLine);

        return requestLine;
    }

    public static List<String> readHttpHeaders(BufferedReader br) throws IOException {
        List<String> httpHeaders = new ArrayList<>();
        String line = br.readLine();

        while (line != null && !line.equals("")) {
            httpHeaders.add(line);
            line = br.readLine();
            log.debug("header: {}", line);
        }

        return httpHeaders;
    }

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

    public static void write(OutputStream dataOutputStream, HttpResponse httpResponse) throws IOException {
        String headerString = httpResponse.getHeader();
        log.debug("response header: {}", headerString);
        byte[] body = httpResponse.getBody();

        dataOutputStream.write(headerString.getBytes());
        dataOutputStream.write(body, 0, body.length);
        dataOutputStream.flush();
    }
}
