package request;

public enum ReqHeaders {

    CONTENT_LENGTH("Content-Length");

    private final String key;

    ReqHeaders(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
