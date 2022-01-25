package util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {

    private final Map<String, Object> cookies;
    private final Map<String, String> headers;
    private HttpStatus httpStatus;
    private byte[] body;

    public HttpResponse(){
        this.cookies = new LinkedHashMap<>();
        this.headers = new LinkedHashMap<>();
    }

    public void setStatus(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }

    public void setCookie(String key, Object value) {
        cookies.put(key, value.toString());
    }

    public void setHeader(String key, String value){
        headers.put(key, value);
    }

    public void setBody(byte[] body){
        this.body = body;
    }

    public Map<String, Object> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public byte[] getBody() {
        return body;
    }

    public String headerText() {
        String res = "";
        String statusLine = String.format("HTTP/1.1 %s %s", httpStatus.getCode(), httpStatus.getMessage());
        String headerString = headers.entrySet().stream()
                .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
                .collect(Collectors.joining("\r\n"));
        res = statusLine + "\r\n" +
                headerString + "\r\n";

        if(cookies.size() != 0){
            String cookieString = cookies.entrySet().stream()
                    .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                    .collect(Collectors.joining(";"));
            res += ("Set-Cookie: " + cookieString + "\r\n");
        }
        res += "\r\n";
        return res;

    }
}
