package http.response;

import util.Constant;

public class ResponseStatusLine {

    private final String protocol;
    private final String status;

    public ResponseStatusLine(String protocol, String status) {
        this.protocol = protocol;
        this.status = status;
    }

    public String getLine() {
        return protocol + " " + status + Constant.lineBreak;
    }
}
