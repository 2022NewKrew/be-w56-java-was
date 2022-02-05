package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import static util.RequestPathUtils.extractRequestURL;

public class IOUtils {
    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

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
     * @param BufferedReader : Request Header READER
     * */
    public static String readHeader(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = readHeaderPath(bufferedReader);

        StringBuilder sb = new StringBuilder();

        while (!"".equals(line)) {
            readHeaderLine(bufferedReader, sb);

            if(line == null){
                log.info("Buffer finish");
                break;
            }
        }
        return sb.toString();
    }

    /**
     * @param BufferedReader : Request Header READER
     * */
    private static String readHeaderPath(BufferedReader bufferedReader) throws IOException {

        String line = bufferedReader.readLine();
        String requestURL = extractRequestURL(line);

        return requestURL;
    }

    private static void readHeaderLine(BufferedReader bufferedReader, StringBuilder sb ) throws IOException {

        String line = bufferedReader.readLine();
        sb.append(line);
        log.info("Buffer read: {}",line);

    }



}
