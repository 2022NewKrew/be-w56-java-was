package webserver.request;

import java.io.IOException;
import java.util.Objects;

public class HttpRequestLine {
    private final String method;
    private final String url;
    private final String requestUrl;
    private final String protocol;

    private HttpRequestLine(String method, String url, String requestUrl, String protocol) {
        this.method = method;
        this.url = url;
        this.requestUrl = requestUrl;
        this.protocol = protocol;
    }

    public static HttpRequestLine makeHttpRequestLine(String firstLine) throws IOException{
        if(Objects.isNull(firstLine))
            throw new IOException("HttpRequestLine 이 없는 잘못된 request 를 요청하였습니다.");

        String[] firstLineSplit = firstLine.split(" ");
        if(firstLineSplit.length != 3)
            throw new IOException("HttpRequestLine 의 paramater는 \"{METHOD} {URL} {PROTOCOL}\" 형식이 나와야 합니다.");

        return new HttpRequestLine(firstLineSplit[0], firstLineSplit[1].split("\\?")[0], firstLineSplit[1], firstLineSplit[2]);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getRequestUrl() {
        return requestUrl;
    }
}
