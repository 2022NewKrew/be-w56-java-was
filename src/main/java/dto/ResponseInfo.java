package dto;

import java.util.List;

public class ResponseInfo {

    private final String requestPath;
    private final HttpResponseStatus status;
    private final List<String> addedHeaders;

    private ResponseInfo(String requestPath, HttpResponseStatus status, List<String> addedHeaders) {
        this.requestPath = requestPath;
        this.status = status;
        this.addedHeaders = addedHeaders;
    }

    public static ResponseInfo valueOf(String requestPath, HttpResponseStatus status, List<String> addedHeaders) {
        return new ResponseInfo(requestPath, status, addedHeaders);
    }

    public String getRequestPath() { return this.requestPath; }
    public HttpResponseStatus getStatus() { return this.status; }
    public List<String> getAddedHeaders() { return this.addedHeaders; }
}
