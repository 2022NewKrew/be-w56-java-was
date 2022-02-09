package webserver.mapper;

import webserver.exception.BadRequestException;
import webserver.exception.InvalidMethodException;
import webserver.exception.ResourceNotFoundException;
import webserver.handler.RequestExceptionHandler;
import webserver.provider.ResponseProvider;
import webserver.http.MIME;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.net.URI;

public class RequestMapper {

    public static HttpResponse process(HttpRequest request) {
        URI uri = request.uri();
        String query = uri.getQuery();
        String path = uri.getPath();
        MIME mime = MIME.from(path);

        if (mime.isStaticResource() && query == null) {
            return ResponseProvider.responseStaticResource(path);
        }
        return handleRequest(request, path);
    }

    private static HttpResponse handleRequest(HttpRequest request, String path) {
        if (!RequestMappingInfo.hasPath(path)) {
            return RequestExceptionHandler.handle(new ResourceNotFoundException("에러: 존재하지 않은 리소스입니다."));
        }
        try {
            RequestMappingInfo requestMappingInfo = RequestMappingInfo.from(path);
            return handleIfValidMethod(requestMappingInfo, request);
        } catch (IllegalArgumentException e) {
            return RequestExceptionHandler.handle(new BadRequestException(e.getMessage()));
        } catch (Exception e) {
            return RequestExceptionHandler.handle(e);
        }
    }

    private static HttpResponse handleIfValidMethod(RequestMappingInfo requestMappingInfo, HttpRequest request) throws Exception {
        if (requestMappingInfo.isNotEqualMethod(request.method())) {
            throw new InvalidMethodException("에러: 부적절한 요청 메서드입니다.");
        }
        return requestMappingInfo.handle(request);
    }
}
