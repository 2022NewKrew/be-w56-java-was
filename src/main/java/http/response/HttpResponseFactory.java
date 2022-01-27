package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewMaker;

public class HttpResponseFactory {

    public static HttpResponse getHttpResponse(Map<String, String> result,
            Map<String, String> model, DataOutputStream dos) throws IOException {
        try {
            StatusCode statusCode = StatusCode.getStatusString(result.get("status"));
            return getResponse(statusCode, result, model, dos);
        } catch (IOException exception) {
            return new HttpResponse404(dos, ViewMaker.getNotFoundView());
        }
    }

    private static HttpResponse getResponse(StatusCode statusCode, Map<String, String> result,
            Map<String, String> model, DataOutputStream dos) throws IOException {
        Logger logger = LoggerFactory.getLogger(HttpResponseFactory.class);
        logger.info("statusCode : {}, result : {}", statusCode.toString(), result.toString());
        byte[] body = ViewMaker.getView(result.get("url"), model);
        List<String> splitResult = List.of(result.get("url").split("\\."));
        int length = splitResult.size();
        String extension = splitResult.get(length - 1);
        switch (statusCode) {
            case OK:
                return new HttpResponse200(dos, ContentType.getContentType(extension), body);
            case FOUND:
                return new HttpResponse302(dos, result.get("url"));
            case UNAUTHORIZED:
                return new HttpResponse401(dos, ContentType.getContentType(extension), body);
            default:
                return new HttpResponse404(dos, ViewMaker.getNotFoundView());
        }
    }
}
