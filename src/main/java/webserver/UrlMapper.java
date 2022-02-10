package webserver;

import http.request.HttpRequestMethod;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponse.HttpResponseBuilder;
import http.response.HttpStatusCode;

public class UrlMapper {

    private static final Map<String, Function<HttpRequest, HttpResponse>>
            urlMap = new ConcurrentHashMap<>();

    private UrlMapper() {}

    public static Function<HttpRequest, HttpResponse> get(String url, HttpRequestMethod method) {
        String key = getKey(url, method);
        if (!urlMap.containsKey(key)) {
            return httpRequest ->  new HttpResponseBuilder(HttpStatusCode.NOT_FOUND).build();
        }
        return urlMap.get(key);
    }

    public static void put(String url, HttpRequestMethod method, Function<HttpRequest, HttpResponse> func) {
        String key = getKey(url, method);
        urlMap.put(key, func);
    }

    private static String getKey(String url, HttpRequestMethod method) {
        return url + " " + method.toString();
    }

    @Override
    public String toString() {
        return "UrlMapper{" + urlMap + '}';
    }
}
