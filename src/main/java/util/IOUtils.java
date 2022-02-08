package util;

import org.apache.tika.Tika;

import java.io.BufferedReader;
import java.io.File;
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
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static String getContentType(File file) throws IOException {
        return new Tika().detect(file);
    }

    public static String getHttpRequestHeader(BufferedReader bufferedReader) throws IOException{
        StringBuilder httpRequestHeader = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if(line==null || "".equals(line))
                break;
            httpRequestHeader.append(line);
            httpRequestHeader.append(System.lineSeparator());
        }
        return httpRequestHeader.toString();
    }
}
