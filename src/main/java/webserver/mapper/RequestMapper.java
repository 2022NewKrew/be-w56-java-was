package webserver.mapper;

import webserver.exception.BadRequestException;
import webserver.exception.InvalidMethodException;
import webserver.exception.ResourceNotFoundException;
import webserver.exception.WebServerException;
import webserver.provider.ResponseProvider;
import webserver.http.MIME;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.URI;

import static webserver.mapper.RequestMappingInfo.isNotValidMethod;

public class RequestMapper {

    public static HttpResponse process(HttpRequest request, OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        URI uri = request.uri();
        String query = uri.getQuery();
        String path = uri.getPath();
        MIME mime = MIME.from(path);

        if (mime.isStaticResource() && query == null) {
            return ResponseProvider.responseStaticResource(dos, path);
        }
        return handleRequest(request, dos, path);
    }

    private static HttpResponse handleRequest(HttpRequest request, DataOutputStream dos, String path) {
        if (!RequestMappingInfo.hasPath(path)) {
            throw new ResourceNotFoundException("에러: 존재하지 않은 리소스입니다.");
        }
        try {
            RequestMappingInfo requestMappingInfo = RequestMappingInfo.from(path);
            return handleIfValidMethod(requestMappingInfo, request, dos);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new BadRequestException("에러: 부적절한 요청입니다.");
        } catch (WebServerException e) {
            throw e;
        } catch (Exception e) {
            throw new WebServerException();
        }
    }

    private static HttpResponse handleIfValidMethod(RequestMappingInfo requestMappingInfo, HttpRequest request, DataOutputStream dos) throws Exception {
        if (isNotValidMethod(requestMappingInfo, request.method())) {
            throw new InvalidMethodException("에러: 부적절한 요청 메서드입니다.");
        }
        return requestMappingInfo.handle(request, dos);
    }
}
