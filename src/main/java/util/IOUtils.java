package util;

import http.HttpHeaders;
import http.MediaType;
import http.request.HttpRequest;
import http.request.RequestBody;
import http.request.RequestLine;
import http.response.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtils {

    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

    private IOUtils() {
    }

    public static HttpRequest readRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        RequestLine requestLine = HttpRequestUtils.parseRequestLine(readRequestLine(br));
        HttpHeaders headers = HttpRequestUtils.parseHeaders(readHttpHeaders(br));

        MediaType contentType = headers.getContentType();
        int contentLength = headers.getContentLength();
        String bodyString = readData(br, contentLength);
        RequestBody body = HttpRequestUtils.parseRequestBody(contentType, bodyString);

        return new HttpRequest(requestLine, headers, body);
    }

    private static String readRequestLine(BufferedReader br) throws IOException {
        String requestLine = br.readLine();
        log.debug("request line: {}", requestLine);

        return requestLine;
    }

    private static List<String> readHttpHeaders(BufferedReader br) throws IOException {
        List<String> httpHeaders = new ArrayList<>();
        String line = br.readLine();

        while (line != null && !line.equals("")) {
            httpHeaders.add(line);
            line = br.readLine();
            log.debug("header: {}", line);
        }

        return httpHeaders;
    }

    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    private static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        log.debug("body: {}", String.copyValueOf(body));
        return String.copyValueOf(body);
    }

    public static void write(OutputStream dataOutputStream, HttpResponse httpResponse) throws IOException {
        String headerString = httpResponse.getHeader();
        log.debug("response header: {}", headerString);
        byte[] body = httpResponse.getBody();

        dataOutputStream.write(headerString.getBytes());
        dataOutputStream.write(body, 0, body.length);
        dataOutputStream.flush();
    }
}
