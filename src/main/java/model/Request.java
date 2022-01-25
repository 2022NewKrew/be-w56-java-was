package model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static util.punctuationMarksUtils.QUESTION_MARK;

@Data
@Builder
public class Request {
    private String[] requestLine;
    private Map<String, String> header;
    private Map<String, String> queryString;

    public static Request of(String[] requestLine, Map<String, String> header, Map<String, String> queryString) {
        return Request.builder()
                .requestLine(requestLine)
                .header(header)
                .queryString(queryString)
                .build();
    }

    public String getUrlPath() {
        return requestLine[1].split(QUESTION_MARK)[0];
    }

    public String getRespContextType() {
        return header.get("Accept").split(",")[0];
    }
}
