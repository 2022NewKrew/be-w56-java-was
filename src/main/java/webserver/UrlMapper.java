package webserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponse.HttpResponseBuilder;
import http.response.HttpStatusCode;

public class UrlMapper {

    private static final Map<String, Function<HttpRequest, HttpResponse>>
            URLS = new ConcurrentHashMap<>();

    private UrlMapper() {

    }

    public static Function<HttpRequest, HttpResponse> get(String url, String method) {
        String key = getKey(url, method);
        if (!URLS.containsKey(key)) {
            return httpRequest ->  new HttpResponseBuilder(HttpStatusCode.NOT_FOUND).build();
        }
        return URLS.get(key);
    }

    public static void put(String url, String method, Function<HttpRequest, HttpResponse> func) {
        String key = getKey(url, method);
        URLS.put(key, func);
    }

    private static String getKey(String url, String method) {
        return url + " " + method;
    }

    @Override
    public String toString() {
        return "UrlMapper{" + URLS + '}';
    }
}
