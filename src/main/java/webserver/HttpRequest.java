package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String requestUri;
    private String httpVersion;
    private Map<String, String> params = null;

    public static HttpRequest from(InputStream in) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        String startLine = bufferedReader.readLine();
        httpRequest.setStartLine(startLine);

//        String line = bufferedReader.readLine();
//        while(!"".equals(line) && line != null) {
//            line = bufferedReader.readLine();
//        }

        return httpRequest;
    }

    private void setStartLine(String startLine) {
        String[] parsedLine = HttpRequestUtils.parseBySpace(startLine);
        this.method = parsedLine[0];
        setUrlInfo(parsedLine[1]);
        this.httpVersion = parsedLine[2];
    }

    private void setUrlInfo(String url) {
        String[] split = url.split("\\?");
        this.requestUri = split[0];
        if(split.length == 2) {
            this.params = HttpRequestUtils.parseQueryString(split[1]);
        }
    }


    public String getMethod() {
        return method;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
