package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private static String readStartLine(BufferedReader bufferedReader) throws IOException {
        return bufferedReader.readLine();
    }

    private static List<String> readHeader(BufferedReader bufferedReader) throws IOException {
        List<String> headerLineList = new ArrayList<>();
        String line = bufferedReader.readLine();
        while (!line.isEmpty()) {
            headerLineList.add(line);
            line = bufferedReader.readLine();
        }
        return headerLineList;
    }

    private static String readBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        return IOUtils.readData(bufferedReader, contentLength);
    }

    private static int getContentLength(Header header) {
        String contentLength = header.get(HttpHeader.CONTENT_LENGTH);

        if (Objects.isNull(contentLength)) {
            return 0;
        }
        return Integer.parseInt(contentLength);
    }

    public static HttpRequest of(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        StartLine startLine = StartLine.of(readStartLine(bufferedReader));
        Header header = Header.of(readHeader(bufferedReader));
        Body body = Body.of(readBody(bufferedReader, getContentLength(header)));

        return new HttpRequest(startLine, header, body);
    }

    private final StartLine startLine;
    private final Header header;
    private final Body body;

    private HttpRequest(StartLine startLine, Header header, Body body) {
        log.debug("HttpRequest: \n{}\n {}\n {}", startLine, header, body);
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public HttpMethod getHttpMethod() {
        return startLine.getHttpMethod();
    }

    public String getBody() {
        return body.get();
    }

    public String getUrl() {
        return startLine.getUrl();
    }

    public Map<String, String> getQuery() {
        return startLine.getQuery();
    }
}
