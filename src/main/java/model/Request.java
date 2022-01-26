package model;

import lombok.Builder;
import lombok.Getter;

import java.util.*;

@Getter
public class Request {
    private List<String> requestLine;
    private Map<String, String> header;
    private Map<String, String> body;


    @Builder
    public Request(List<String> requestLine, Map<String, String> header, Map<String, String> body){
        this.requestLine = requestLine;
        this.header = header;
        this.body = body;
    }

    public static Request of(List<String> requestLine, Map<String, String> header, Map<String, String> body) {
        return Request.builder()
                .requestLine(requestLine)
                .header(header)
                .body(body)
                .build();
    }

    public String getUrlPath() {
        return requestLine.get(1).split("\\?")[0];
    }

    public String getContextType() {
        return header.get("Accept").split(",")[0];
    }

    public String getHttpMethod() {
        return requestLine.get(0);
    }
}
