package model;

public class RequestLine {
    private final String httpMethod;
    private final String url;
    private final String httpVersion;


    public RequestLine(String[] reqLine) {
        this.httpMethod = reqLine[0];
        this.url = reqLine[1];
        this.httpVersion = reqLine[2];
    }

    @Override
    public String toString() {
        return "httpMethod='" + httpMethod + '\'' +
                ", url='" + url + '\'' +
                ", httpVersion='" + httpVersion + '\'';
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
