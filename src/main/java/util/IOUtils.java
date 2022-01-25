package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * @param br
     *  BufferedReader 는 Request Body 를 시작하는 시점이어야
     * @param contentLength
     *  contentLength 는 Request Header 의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String readStartLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.debug("request : {}", line);
        return line;
    }

    public static List<String> readHeader(BufferedReader br) throws IOException {
        List<String> header = new ArrayList<>();
        String line;
        while(!(line = br.readLine()).equals("")) {
            header.add(line);
            log.debug("request : {}", line);
        }
        return header;
    }
}
