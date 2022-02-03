package webserver.http;

import http.*;
import util.HttpRequestUtils;
import util.StringUtils;
import webserver.exception.InternalServerErrorException;
import webserver.http.request.RequestBodyResolver;
import webserver.http.request.RequestBody;
import webserver.http.response.ResponseBodyResolver;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpEntityConverter {

    private final List<RequestBodyResolver> requestBodyResolvers;
    private final List<ResponseBodyResolver> responseBodyResolvers;

    public HttpEntityConverter(List<RequestBodyResolver> requestBodyResolvers, List<ResponseBodyResolver> responseBodyResolvers) {
        this.requestBodyResolvers = requestBodyResolvers;
        this.responseBodyResolvers = responseBodyResolvers;
    }

    public RequestEntity<?> convertRequest(HttpRequest httpRequest, Type type) {
        RequestBody<?> body = createRequestBody(httpRequest, type);
        HttpHeaders headers = httpRequest.getHeaders();
        RequestParams params = createRequestParams(httpRequest);
        List<Cookie> cookies = createCookies(headers);
        return new RequestEntity<>(headers, params, cookies, body);
    }

    public HttpResponse convertResponse(ResponseEntity<?> responseEntity) {
        byte[] body = createResponseBody(responseEntity);
        HttpHeaders headers = responseEntity.getHttpHeaders();
        StatusCode statusCode = responseEntity.getStatusCode();
        setResponseHeaderCookies(headers, responseEntity.getCookies(), "/");
        return new HttpResponse(headers, statusCode, body);
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

    private RequestBody<?> createRequestBody(HttpRequest httpRequest, Type type) {
        for(RequestBodyResolver requestBodyResolver : requestBodyResolvers) {
            if (requestBodyResolver.supports(type)) {
                return requestBodyResolver.resolveRequestBody(httpRequest, type);
            }
        }
        throw new InternalServerErrorException("적합한 Request Body Renderer가 없습니다.");
    }

    private byte[] createResponseBody(ResponseEntity<?> responseEntity) {
        for(ResponseBodyResolver resolver : responseBodyResolvers) {
            if(resolver.supports(responseEntity)) {
                return resolver.resolve(responseEntity);
            }
        }
        throw new InternalServerErrorException("적절한 ResponseBodyRenderer가 없습니다.");
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
