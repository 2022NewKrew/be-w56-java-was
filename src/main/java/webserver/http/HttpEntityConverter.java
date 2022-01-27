package webserver.http;

import http.*;
import util.HttpRequestUtils;
import util.StringUtils;
import webserver.http.request.RequestBodyResolver;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpEntityConverter {

    private List<RequestBodyResolver> requestBodyResolvers;

    public HttpEntityConverter(List<RequestBodyResolver> requestBodyResolvers) {
        this.requestBodyResolvers = requestBodyResolvers;
    }

    public RequestEntity<?> convertRequest(HttpRequest httpRequest, Type type) {
        HttpHeaders headers = httpRequest.getHeaders();
        RequestParams params = createRequestParams(httpRequest);
        List<Cookie> cookies = createCookies(headers);
        Object body = createBody(httpRequest, type);
        return new RequestEntity<>(headers, params, cookies, body);
    }

    public <T> HttpResponse convertResponse(ResponseEntity<T> responseEntity) {
        HttpHeaders headers = responseEntity.getHttpHeaders();
        StatusCode statusCode = responseEntity.getStatusCode();
        setResponseHeaderCookies(headers, responseEntity.getCookies(), "/");
        String view = responseEntity.getView();
        if(view.startsWith("redirect:")) {
            headers.addHeader("Location", view.substring(9));
        }
        return new HttpResponse(headers, statusCode, new byte[1]);
    }

    private void setResponseHeaderCookies(HttpHeaders headers, List<Cookie> cookies, String path) {
        if(cookies.size() <= 0)
            return;
        String cookieString = cookies.stream().map(Cookie::toString).collect(Collectors.joining(";"));
        cookieString += "; Path=" + path;
        headers.addHeader("Set-Cookie", cookieString);
    }

    private List<Cookie> createCookies(HttpHeaders headers) {
        String cookies = headers.getHeaderByName("Cookie");
        if(StringUtils.isEmpty(cookies)) {
            return List.of();
        }
        String[] splitCookies = cookies.split(";");
        return Arrays.stream(splitCookies)
                .map(cookie -> createCookie(cookie.trim()))
                .collect(Collectors.toUnmodifiableList());
    }

    private Object createBody(HttpRequest httpRequest, Type type) {
        Object body = null;
        if(httpRequest.hasBody()) {
            for(RequestBodyResolver requestBodyResolver : requestBodyResolvers) {
                if(requestBodyResolver.supports(httpRequest.getContentType(), type)) {
                    body = requestBodyResolver.resolveRequestBody(httpRequest.getBody(), type);
                    break;
                }
            }
        }
        return body;
    }

    private Cookie createCookie(String cookie) {
        String[] splitCookie = cookie.split("=");
        return new Cookie(splitCookie[0], splitCookie[1]);
    }

    private RequestParams createRequestParams(HttpRequest httpRequest) {
        Map<String, String> parseQueryString = HttpRequestUtils.parseQueryString(httpRequest.getQuery());
        if(httpRequest.hasBody() && httpRequest.getContentType().equals(ContentType.APPLICATION_X_WWW_FORM_URLENCODED)) {
            String requestBody = new String(httpRequest.getBody(), StandardCharsets.UTF_8);
            Map<String, String> parseBody = HttpRequestUtils.parseQueryString(requestBody);
            parseQueryString.putAll(parseBody);
        }
        return new RequestParams(parseQueryString);
    }
}
