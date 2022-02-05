package util;

import java.io.*;
import java.nio.file.Files;

import DTO.HeaderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * @return*/
    public static HeaderDTO readHeader(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = "init";
        HeaderDTO headerDTO = new HeaderDTO();

        while (!"".equals(line)) {
            line = readHeaderLine(bufferedReader, headerDTO);

            if(line == null){
                log.info("Buffer finish");
                break;
            }
        }
        return headerDTO;
    }

    public static byte[] readHeaderPathFile(String firstLine) throws IOException {

        String requestURL = extractRequestURL(firstLine);
        byte[] body = IOUtils.readFileByte(requestURL);

        return body;
    }

    private static String readHeaderLine(BufferedReader bufferedReader, HeaderDTO headerDTO) throws IOException {
        String line = bufferedReader.readLine();
        headerDTO.addBufferLine(line);
        log.info("Buffer read: {}",line);
        return line;

    }

    public static byte[] readFileByte(String url) throws IOException {

        byte[] byteBody = Files.readAllBytes(new File("./webapp" + url).toPath());
        //log.info("File Body Bytes\n: {}", byteBody);
        //String strBody = byteToStr(byteBody);
        return byteBody;
    }

    //helper function  -- not used
    private static String byteToStr(byte[] body){
        String strBody = new String(body); // convert byte array to string
        log.info("File Body Bytes\n: {}", strBody);
        return strBody;
    }



}
