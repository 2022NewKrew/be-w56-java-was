package util;

import web.http.request.HttpRequestBody;
import web.http.request.HttpRequestHeaders;
import web.http.request.HttpRequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

    /**
     * @param br Request Body를 시작하는 시점 BufferedReader
     * @param contentLength Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static HttpRequestBody readRequestBody(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return new HttpRequestBody(URLDecoder.decode(String.copyValueOf(body), StandardCharsets.UTF_8));
    }

    /**
     * @param br Request 요청을 입력받을 때의 BufferedReader
     * @return HttpRequestLine 정보
     * @throws IOException
     */
    public static HttpRequestLine readRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        log.info("=========== Request Line ===========");
        log.info("{}", requestLine);
        String[] tokens = requestLine.split(" ");
        return new HttpRequestLine(tokens[0], tokens[1], tokens[2]);
    }

    /**
     * @param br Request 요청을 입력받을 때의 BufferedReader
     * @return HttpRequestHeaders 정보
     * @throws IOException
     */
    public static HttpRequestHeaders readRequestHeaders(BufferedReader br) throws IOException {
        List<Pair> headers = new ArrayList<>();
        String line;
        log.info("========== Request Header ==========");
        while (!(line = br.readLine()).equals("")) {
            log.info(" {}", line);
            Pair header = HttpRequestUtils.parseHeader(line);
            headers.add(header);
        }
        log.info("====================================");
        return new HttpRequestHeaders(headers);
    }
}
