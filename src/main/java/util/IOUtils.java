package util;

import http.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {

    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

    private IOUtils() {
    }

    public static List<String> getRequestLines(InputStream in) throws IOException {
        List<String> header = new ArrayList<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = br.readLine();
        log.debug("request line: {}", line);

        while (line != null && !line.equals("")) {
            header.add(line);
            line = br.readLine();
            log.debug("header: {}", line);
        }

        return header;
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
