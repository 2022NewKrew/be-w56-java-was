package webserver.http.response;

public class CustomHttpResponse {
    private CustomResponseBody body;
    private CustomResponseHeader responseHeader;
    private CustomHttpStatus httpStatus;

    public CustomHttpResponse() {
        body = new CustomResponseBody();
        responseHeader = new CustomResponseHeader();
        httpStatus = CustomHttpStatus.OK;
    }

    public String getResponseHeaderContent() {
        return responseHeader.getResponseHeader();
    }

    public CustomHttpStatus getHttpStatus() {
        return httpStatus;
    }

    public CustomResponseBody getBody() {
        return body;
    }

    public void setHttpStatus(CustomHttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setBody(byte[] bodyContent) {
        body.writeBody(bodyContent);
    }

    public void writeHeader(String headerContent) {
        responseHeader.addHeader(headerContent);
    }
}
