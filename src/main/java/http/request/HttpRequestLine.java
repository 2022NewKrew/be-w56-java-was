package http.request;

public class HttpRequestLine {

    private String method;
    private HttpRequestUri requestUri;
    private String httpVersion;

    public HttpRequestLine(String requestLine) {
        String[] parsedRequestLine = requestLine.split(" ");
        method = parsedRequestLine[0];
        requestUri = new HttpRequestUri(parsedRequestLine[1]);
        httpVersion = parsedRequestLine[2];
    }

    public String getPath() {
        return requestUri.getPath();
    }

    public String getParam(String paramName) {
        return requestUri.getParam(paramName);
    }
}
