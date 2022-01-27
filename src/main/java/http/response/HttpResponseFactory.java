package http.response;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import util.Constant;
import view.ViewMaker;

public class HttpResponseFactory {

    public static HttpResponse getHttpResponse(Map<String, String> result,
            Map<String, String> cookie, Map<String, String> model, DataOutputStream dos) {

        String url = result.get("url");
        StatusCode statusCode = StatusCode.getStatusString(result.get("status"));
        String statusText = statusCode.getStatus();

        ResponseStatusLine statusLine = new ResponseStatusLine(Constant.protocol, statusText);
        ResponseHeader header = new ResponseHeader();

        switch (statusCode) {
            case OK:
                return getHttpResponse200(statusLine, header, url, cookie, dos);
            case FOUND:
                return getHttpResponse302(statusLine, header, url, cookie, dos);
            case UNAUTHORIZED:
                return getHttpResponse401(statusLine, header, url, cookie, dos);
            default:
                return getHttpResponse404(statusLine, header, url, cookie, dos);
        }
    }

    public static HttpResponse getHttpResponse(Map<String, String> result,
            Map<String, String> model, DataOutputStream dos) {

        return getHttpResponse(result, new HashMap<>(), model, dos);
    }

    private static String getContentType(String url) {
        List<String> splitResult = List.of(url.split("\\."));
        if (splitResult.size() == 1) {
            return ContentType.DEFAULT.getType();
        }
        String extension = splitResult.get(splitResult.size() - 1);
        return ContentType.getContentType(extension);
    }

    private static void addCookie(ResponseHeader header, Map<String, String> cookie) {
        if(cookie.isEmpty()){
            return;
        }
        for (Entry<String, String> entry : cookie.entrySet()) {
            header.addComponent("Set-Cookie", entry.getKey() + "=" + entry.getValue());
        }
    }

    private static HttpResponse getHttpResponse200(ResponseStatusLine statusLine,
            ResponseHeader header, String url, Map<String, String> cookie, DataOutputStream dos) {
        ResponseBody body = getResponseBody(header, url);
        addCookie(header, cookie);
        return new HttpResponse(statusLine, header, body, dos);
    }

    private static HttpResponse getHttpResponse302(ResponseStatusLine statusLine,
            ResponseHeader header, String url, Map<String, String> cookie, DataOutputStream dos) {
        ResponseBody body = new ResponseBody(ViewMaker.getView(url, new HashMap<>()));
        header.addComponent("Location", url);
        addCookie(header, cookie);
        return new HttpResponse(statusLine, header, body, dos);
    }

    private static HttpResponse getHttpResponse401(ResponseStatusLine statusLine,
            ResponseHeader header, String url, Map<String, String> cookie, DataOutputStream dos) {
        ResponseBody body = getResponseBody(header, url);
        addCookie(header, cookie);
        return new HttpResponse(statusLine, header, body, dos);
    }

    private static HttpResponse getHttpResponse404(ResponseStatusLine statusLine,
            ResponseHeader header, String url, Map<String, String> cookie, DataOutputStream dos) {
        ResponseBody body = new ResponseBody(ViewMaker.getNotFoundView());
        header.addComponent("Content-Type", getContentType(url));
        header.addComponent("Content-Length", String.valueOf(body.getBodyLength()));
        addCookie(header, cookie);
        return new HttpResponse(statusLine, header, body, dos);
    }

    private static ResponseBody getResponseBody(ResponseHeader header, String url) {
        ResponseBody body = new ResponseBody(ViewMaker.getView(url, new HashMap<>()));
        header.addComponent("Content-Type", getContentType(url));
        header.addComponent("Content-Length", String.valueOf(body.getBodyLength()));
        return body;
    }
}
