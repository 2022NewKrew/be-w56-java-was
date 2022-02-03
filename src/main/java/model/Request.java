package model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

import static util.punctuationMarksUtils.QUESTION_MARK;

@Data
@Builder
public class Request {
    private String[] requestLine;
    private Map<String, String> header;
    private Map<String, String> queryString;
    private Map<String, String> cookies;

    public static Request of(String[] requestLine, Map<String, String> header, Map<String, String> queryString, Map<String, String> cookies) {
        return Request.builder()
                .requestLine(requestLine)
                .header(header)
                .queryString(queryString)
                .cookies(cookies)
                .build();
    }

    public String getUrlPath() {
        return requestLine[1].split(QUESTION_MARK)[0];
    }

    public String getRespContextType() {
        return header.get("Accept").split(",")[0];
    }

    public String getHttpMethod() {
        return requestLine[0];
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
}
