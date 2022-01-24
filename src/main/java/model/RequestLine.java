package model;

public class RequestLine {
    private final String request;
    private final String url;
    private final String http;

    private RequestLine(String request, String url, String http) {
        this.request = request;
        this.url = url;
        this.http = http;
    }

    public static RequestLine from(String line) {
        String[] tokens = line.split(" ");
        return new RequestLine(tokens[0], tokens[1], tokens[2]);
    }

    public String getRequest() {
        return request;
    }

    public String getUrl() {
        return url;
    }

    public String getHttp() {
        return http;
    }

    @Override
    public String toString() {
        return "RequestLine{" +
                "request='" + request + '\'' +
                ", url='" + url + '\'' +
                ", http='" + http + '\'' +
                '}';
    }
}
