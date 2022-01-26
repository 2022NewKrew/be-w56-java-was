package webserver;

import webserver.http.MIME;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class RequestMapper {

    public static MyHttpResponse process(MyHttpRequest request, OutputStream out) {
        String path = request.uri().getPath();
        DataOutputStream dos = new DataOutputStream(out);

        boolean isRequestStaticResource = Arrays.stream(MIME.values())
                .anyMatch(m -> m.isExtensionMatch(path));

        if (isRequestStaticResource) {
            return ResponseProvider.responseStaticResource(dos, path);
        }

        return RequestMappingInfo.handleRequest(request, dos, path);
    }
}
