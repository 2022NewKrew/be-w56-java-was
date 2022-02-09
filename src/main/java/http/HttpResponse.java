package http;

import enums.HttpProtocol;
import enums.HttpStatusCode;

public class HttpResponse {

    private HttpProtocol protocol;
    private HttpStatusCode statusCode;
    private byte[] body;
    private String responseContentType;
    private String responseDataPath;
    private String redirectUrl;

    public HttpProtocol getProtocol() {
        return protocol;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public String getResponseDataPath() {
        return responseDataPath;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setProtocol(HttpProtocol protocol) {
        this.protocol = protocol;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public void setResponseDataPath(String responseDataPath) {
        this.responseDataPath = responseDataPath;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
