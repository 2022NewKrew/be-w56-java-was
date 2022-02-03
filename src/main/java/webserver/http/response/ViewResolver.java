package webserver.http.response;

import webserver.file.DocumentRoot;
import webserver.http.ResponseEntity;

import java.nio.charset.StandardCharsets;

import static webserver.http.response.View.*;

public class ViewResolver implements ResponseBodyResolver {

    private final DocumentRoot documentRoot;

    public ViewResolver(DocumentRoot documentRoot) {
        this.documentRoot = documentRoot;
    }

    public boolean supports(ResponseEntity<?> responseEntity) {
        ResponseBody<?> responseBody = responseEntity.getResponseBody();
        return View.class.isAssignableFrom(responseBody.getClass());
    }

    @Override
    public byte[] resolve(ResponseEntity<?> responseEntity) {
        View responseBody = (View) responseEntity.getResponseBody();
        byte[] bytes = null;
        if (responseBody instanceof StaticView) {
            String filePath = responseBody.getResponseValue();
            bytes = documentRoot.readFileByPath(filePath);
        }
        if (responseBody instanceof BodyView) {
            String body = responseBody.getResponseValue();
            bytes = body.getBytes(StandardCharsets.UTF_8);
        }
        responseEntity.addHeaders("Content-Length", String.valueOf(bytes.length));
        responseEntity.addHeaders("Content-Type", "text/html;charset=utf-8");
        return bytes;
    }
}
