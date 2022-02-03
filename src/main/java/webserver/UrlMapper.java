package webserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import request.HttpRequest;
import response.HttpResponse;
import response.HttpResponse.HttpResponseBuilder;
import response.HttpStatusCode;

public class UrlMapper {

    private static final UrlMapper INSTANCE = new UrlMapper();
    private final Map<String, Function<HttpRequest, HttpResponse>> urlMap;

    private UrlMapper() {
        urlMap = new ConcurrentHashMap<>();
    }

    public static UrlMapper getInstance() {
        return INSTANCE;
    }

    public Function<HttpRequest, HttpResponse> get(String url, String method) {
        String key = getKey(url, method);
        if (!urlMap.containsKey(key)) {
            return httpRequest ->  new HttpResponseBuilder(HttpStatusCode.NOT_FOUND).build();
        }
        return urlMap.get(key);
    }

    public void put(String url, String method, Function<HttpRequest, HttpResponse> func) {
        String key = getKey(url, method);
        urlMap.put(key, func);
    }

    private static String getKey(String url, String method) {
        return url + " " + method;
    }

    @Override
    public String toString() {
        return "UrlMapper{" +
            "urlMap=" + urlMap +
            '}';
    }

    //    public void replace(String url, String method, Function<HttpRequest, HttpResponse> func) {
//        urlMap.replace(new String[] {url, method.toUpperCase()}, func);
//    }

//    public void remove(String url, String method) {
//        urlMap.remove(new String[] {url, method.toUpperCase()});
//    }
}
