package model;

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

    private static List<String> getHeaderLineList(List<String> requestLineList, int bodyIndex) {
        List<String> headerLineList = new ArrayList<>();
        for (int i = 1; i < bodyIndex; i++) {
            headerLineList.add(requestLineList.get(i));
        }
        return headerLineList;
    }

    private static List<String> getBodyLineList(List<String> requestLineList, int bodyIndex) {
        List<String> bodyLineList = new ArrayList<>();
        for (int i = bodyIndex + 1; i < requestLineList.size(); i++) {
            bodyLineList.add(requestLineList.get(i));
        }
        return bodyLineList;
    }

    public static HttpRequest of(List<String> requestLineList) {
        StartLine startLine = StartLine.of(requestLineList.get(0));

        int bodyIndex = getBodyIndex(requestLineList);
        Header header = Header.of(getHeaderLineList(requestLineList, bodyIndex));
        Body body = Body.of(getBodyLineList(requestLineList, bodyIndex));

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

    public String getPath() {
        return startLine.getUrl();
    }

    public String getHeader(String key) {
        return header.get(key);
    }
}
