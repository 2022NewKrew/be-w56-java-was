package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class HttpRequest {
    private HttpRequestLine httpRequestLine;
    private HttpRequestHeader httpRequestHeader;
    private HttpRequestBody httpRequestBody;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String requestLine = br.readLine();

        this.httpRequestLine = new HttpRequestLine(requestLine);
        this.httpRequestHeader = new HttpRequestHeader(br);
        if (getMethod() == HttpRequestMethod.POST) {
            this.httpRequestBody = new HttpRequestBody(br, getContentLength());
        }
    }

    public HttpRequestMethod getMethod() {
        return httpRequestLine.getMethod();
    }

    public String getUrl() {
        return httpRequestLine.getUrl();
    }

    public Map<String, String> getQueryParams() {
        return httpRequestLine.getQueryParams();
    }

    public Map<String, String> getBodyParams() {
        return httpRequestBody.getBody();
    }

    public String getContentType() {
        return httpRequestHeader.getHeaders().get("Accept").split(",")[0];
    }

    public int getContentLength() {
        return Integer.parseInt(httpRequestHeader.getHeaders().get("Content-Length"));
    }
}
