package request;

public class HttpRequestFactory {

    private static HttpRequestFactory INSTANCE;

    private HttpRequestFactory() {}

    public static HttpRequestFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpRequestFactory();
        }
        return INSTANCE;
    }


}
