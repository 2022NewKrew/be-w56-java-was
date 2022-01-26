package webserver.mapper;

import webserver.provider.ResponseProvider;
import webserver.http.MIME;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.URI;

public class RequestMapper {

    public static MyHttpResponse process(MyHttpRequest request, OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        URI uri = request.uri();
        String query = uri.getQuery();
        String path = uri.getPath();
        MIME mime = MIME.from(path);

        if (mime.isStaticResource() && query == null) {
            return ResponseProvider.responseStaticResource(dos, path);
        }
        return RequestMappingInfo.handleRequest(request, dos, path);
    }
}
