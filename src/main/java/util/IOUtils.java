package util;

import org.apache.tika.Tika;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class IOUtils {
    /**
     * @param br 은
     *            Request Body를 시작하는 시점이어야
     * @param contentLength 는
     *            Request Header의 Content-Length 값이다.
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

    public static String gttHttpRequestBody(BufferedReader br,int contentLength) throws IOException{
        return IOUtils.readData(br,contentLength);
    }

    private static Optional<String> getHeaderParameterValue(String httpRequestHeader,String parameterName) {
        Map<String,String> requestHeaderMap = HttpRequestUtils.parseQueryString(
                httpRequestHeader,System.lineSeparator(),": "
        );

        if(requestHeaderMap.containsKey(parameterName))
            return Optional.of(requestHeaderMap.get(parameterName));
        return Optional.empty();
    }
    public static int getContentLength(String httpRequestHeader){
        Optional<String> contentLengthOptional = getHeaderParameterValue(httpRequestHeader,"Content-Length");
        return contentLengthOptional.map(Integer::parseInt).orElse(0);
    }
}
