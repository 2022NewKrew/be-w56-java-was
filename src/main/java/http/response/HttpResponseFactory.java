package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
        switch (statusCode) {
            case OK:
                byte[] body = ViewMaker.getView(result.get("url"), model);
                List<String> splitResult = List.of(result.get("url").split("\\."));
                int length = splitResult.size();
                String extension = splitResult.get(length - 1);
                return new HttpResponse200(dos, ContentType.getContentType(extension), body);
            case FOUND:
                return new HttpResponse302(dos, result.get("url"));
            default:
                return new HttpResponse404(dos, ViewMaker.getNotFoundView());
        }
    }
}
