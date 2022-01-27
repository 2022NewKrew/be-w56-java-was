package util;

import model.Request;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class IOUtils {
    /*
     *
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

    public static byte[] readFromFile(Request request) throws IOException {
        String url = request.getRequestHeader().getRequestLine().getUrl();
        return Files.readAllBytes(
                new File("webapp/" + url).toPath());
    }

    public static String getExtension(Request request) {
        String url = request.getRequestHeader().getRequestLine().getUrl();
        String[] splitted = url.split("\\.");
        return splitted[splitted.length - 1];
    }
}
