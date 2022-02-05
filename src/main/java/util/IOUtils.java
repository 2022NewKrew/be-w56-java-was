package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        byte[] fileBytes = readFileByte(line);

        StringBuilder sb = new StringBuilder();

        while (!"".equals(line)) {
            line = readHeaderLine(bufferedReader, sb);

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

    private static String readHeaderLine(BufferedReader bufferedReader, StringBuilder sb ) throws IOException {

        String line = bufferedReader.readLine();
        sb.append(line);
        log.info("Buffer read: {}",line);
        return line;

    }

    private static byte[] readFileByte(String url) throws IOException {
        Path filepath = Paths.get("./webapp" + url);
        byte[] byteBody = Files.readAllBytes(filepath);
        //String strBody = byteToStr(byteBody);
        return byteBody;
    }

    private static String byteToStr(byte[] body){
        String strBody = new String(body); // convert byte array to string
        log.info("File Body Bytes\n: {}", strBody);
        return strBody;
    }



}
