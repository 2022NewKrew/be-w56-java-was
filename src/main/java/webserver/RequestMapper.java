package webserver;

import webserver.http.MIME;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class RequestMapper {

    public static MyHttpResponse process(MyHttpRequest request, OutputStream out) {
        String path = request.uri().getPath();
        DataOutputStream dos = new DataOutputStream(out);

        MIME mime = MIME.from(path);

        if (mime.isStaticResource()) {
            return ResponseProvider.responseStaticResource(dos, path);
        }
        return RequestMappingInfo.handleRequest(request, dos, path);
    }
}
