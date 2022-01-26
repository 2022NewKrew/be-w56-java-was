package webserver.http.response;

public class CustomResponseHeader {
    private String responseHeader;

    CustomResponseHeader() {
        responseHeader = "";
    }

    public void addHeader(String content) {
        responseHeader = responseHeader.concat(content);
    }

    public String getResponseHeader() {
        return responseHeader;
    }
}
