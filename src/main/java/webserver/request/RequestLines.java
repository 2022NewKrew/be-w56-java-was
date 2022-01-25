package webserver.request;

import com.google.common.base.Strings;
import exception.IllegalRequestException;

public class RequestLines {
    private static final String REGEX = " ";
    private static final int METHOD_IDX = 0;
    private static final int URL_IDX = 1;

    private String method;
    private String url;

    public RequestLines(){}

    public void parseRequestLine(String requestLine){
        if(Strings.isNullOrEmpty(requestLine)){
            throw new IllegalRequestException("잘못된 HTTP 요청입니다.");
        }

        String[] tokens = requestLine.split(REGEX);
        method = tokens[METHOD_IDX];
        url = tokens[URL_IDX];
    }

    public String getMethod() {
        return method;
    }

    public String getUrl(){
        return url;
    }
}
