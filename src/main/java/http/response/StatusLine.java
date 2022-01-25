package http.response;

public class StatusLine {

    private String protocolVersion;

    private String statusCode;

    private String statusText;

    public void updateProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    };

    public void updateStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void updateStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }
}
