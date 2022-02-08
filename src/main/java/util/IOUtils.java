package util;

import java.io.*;
import java.nio.file.Files;

import DTO.RequestHeader;
import DTO.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    public static RequestHeader readHeader(InputStream in) throws IOException  {
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = "init";
        RequestHeader headerDTO = new RequestHeader();

        while (!"".equals(line)) {
            line = readHeaderLine(bufferedReader, headerDTO);
        }

        readBody(bufferedReader, headerDTO);

        return headerDTO;
    }

    public static void readBody(BufferedReader bufferedReader, RequestHeader headerDTO) throws IOException {
        int contentLeng = headerDTO.getContentLength();

        if (contentLeng > 0){
            log.info("content length : {}", contentLeng);
            String body = readData(bufferedReader, contentLeng);
            log.info("body : {}", body);
            headerDTO.addBody(body);
        }
    }

    public static byte[] readHeaderPathFile(String requestURL, ResponseHeader responseHeader) throws IOException {
        byte[] body = IOUtils.readFileByte(requestURL);
        responseHeader.setContentLength(body.length);
        return body;
    }

    private static String readHeaderLine(BufferedReader bufferedReader, RequestHeader headerDTO) throws IOException {
        String line = bufferedReader.readLine();

        if("".equals(line)){return "";}

        headerDTO.addBufferLine(line);
        log.info("Buffer read: {}",line);
        return line;

    }

    public static byte[] readFileByte(String url) throws IOException {

        byte[] byteBody = Files.readAllBytes(new File("./webapp" + url).toPath());
        return byteBody;
    }





}
