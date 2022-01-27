package http.response;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import view.ViewMaker;

public class HttpResponseFactory {

    private static NewHttpResponse getHttpResponse(StatusCode statusCode,
            Map<String, String> result,
            Map<String, String> model, String protocol, DataOutputStream dos) {

        String url = result.get("url");
        String status = StatusCode
                .getStatusString(result.get("status"))
                .getStatus();
        
        ResponseStatusLine statusLine = new ResponseStatusLine(protocol, status);
        ResponseHeader header = new ResponseHeader();

        switch (statusCode) {
            case OK:
                return getHttpResponse200(statusLine, header, url, dos);
            case FOUND:
                return getHttpResponse302(statusLine, header, url, dos);
            case UNAUTHORIZED:
                return getHttpResponse401(statusLine, header, url, dos);
            default:
                return getHttpResponse404(statusLine, header, url, dos);
        }
    }

    private static String getContentType(String url) {
        List<String> splitResult = List.of(url.split("\\."));
        if (splitResult.size() == 1) {
            return ContentType.DEFAULT.getType();
        }
        String extension = splitResult.get(splitResult.size() - 1);
        return ContentType.getContentType(extension);
    }

    private static NewHttpResponse getHttpResponse200(ResponseStatusLine statusLine,
            ResponseHeader header, String url, DataOutputStream dos) {
        ResponseBody body = getResponseBody(header, url);
        return new NewHttpResponse(statusLine, header, body, dos);
    }

    private static NewHttpResponse getHttpResponse302(ResponseStatusLine statusLine,
            ResponseHeader header, String url, DataOutputStream dos) {
        ResponseBody body = new ResponseBody(ViewMaker.getView(url, new HashMap<>()));
        header.addComponent("Location", url);
        return new NewHttpResponse(statusLine, header, body, dos);
    }

    private static NewHttpResponse getHttpResponse401(ResponseStatusLine statusLine,
            ResponseHeader header, String url, DataOutputStream dos) {
        ResponseBody body = getResponseBody(header, url);
        return new NewHttpResponse(statusLine, header, body, dos);
    }

    private static NewHttpResponse getHttpResponse404(ResponseStatusLine statusLine,
            ResponseHeader header, String url, DataOutputStream dos) {
        ResponseBody body = new ResponseBody(ViewMaker.getNotFoundView());
        header.addComponent("Content-Type", getContentType(url));
        header.addComponent("Content-Length", String.valueOf(body.getBodyLength()));
        return new NewHttpResponse(statusLine, header, body, dos);
    }

    private static ResponseBody getResponseBody(ResponseHeader header, String url) {
        ResponseBody body = new ResponseBody(ViewMaker.getView(url, new HashMap<>()));
        header.addComponent("Content-Type", getContentType(url));
        header.addComponent("Content-Length", String.valueOf(body.getBodyLength()));
        return body;
    }
}
