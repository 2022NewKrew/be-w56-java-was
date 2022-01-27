package http;

public class Url {

    private String path;

    private String queryString;

    private Url(String path, String queryString) {
        this.path = path;
        this.queryString = queryString;
    }

    public static Url of(String url) {
        String[] arr = url.split("\\?");
        String path = arr[0];
        String queryString = arr.length >= 2 ? arr[1] : "";
        return new Url(path, queryString);
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }
}
