package model;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {

    private static final String BODY_SEPARATOR = "";

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private static int getBodyIndex(List<String> requestLineList) {
        for (int i = 0; i < requestLineList.size(); i++) {
            if (Objects.equals(requestLineList.get(i), BODY_SEPARATOR)) {
                return i;
            }
        }
        return requestLineList.size();
    }

    public static List<String> convertToStringList(BufferedReader bufferedReader) throws IOException {
        List<String> requestLineList = new ArrayList<>();
        String line = bufferedReader.readLine();
        while (!Strings.isNullOrEmpty(line)) {
            log.debug("header: {}", line);

            requestLineList.add(line);
            line = bufferedReader.readLine();
        }
        return requestLineList;
    }

    public static HttpRequest of(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        List<String> requestLineList = convertToStringList(bufferedReader);
        StartLine startLine = StartLine.of(requestLineList.get(0));

        int bodyIndex = getBodyIndex(requestLineList);
        Header header = Header.of(requestLineList.subList(1, bodyIndex));
        Body body = Body.of(requestLineList.subList(Math.min(bodyIndex + 1, requestLineList.size()),
                requestLineList.size()));

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

    public String getUrl() {
        return startLine.getUrl();
    }
}
