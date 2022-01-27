package http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import util.Constant;

public class HttpRequestFactory {

    public static HttpRequest getHttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String header = readRequestHeader(bufferedReader);
        if(header.contains("Content-Length")) {
            int bodySize = getBodySize(header);
            return new HttpRequest(header, readRequestBody(bufferedReader, bodySize));
        }
        return new HttpRequest(header, null);
    }

    private static String readRequestHeader(BufferedReader bufferedReader) throws IOException {
        StringBuilder result = new StringBuilder();

        String line;
        while (!(line = bufferedReader.readLine()).equals("")) {
            result.append(line).append(Constant.lineBreak);
        }
        return result.toString();
    }

    private static int getBodySize(String header) {
        List<String> requestLines = List.of(header.split("\r\n"));

        for (String requestLine : requestLines) {
            if(requestLine.contains("Content-Length")){
                return Integer.parseInt(requestLine.split(": ")[1]);
            }
        }

        throw new IllegalArgumentException("BodySize를 찾지 못하였습니다.");
    }

    private static String readRequestBody(BufferedReader bufferedReader, int bodySize)
            throws IOException {
        char[] cbuf = new char[bodySize];
        bufferedReader.read(cbuf);
        return new String(cbuf);
    }
}
