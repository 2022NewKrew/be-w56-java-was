package webserver.http;

public class Cookie {
    private String key;
    private String value;

    public Cookie(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    public String toString() {
        return key + "=" + value;
    }
}
