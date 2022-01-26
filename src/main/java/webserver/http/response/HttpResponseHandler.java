package webserver.http.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.CustomHttpRequest;

import java.util.Map;

public class HttpResponseHandler {
    private CustomHttpResponse httpResponse;
    private static final Logger log = LoggerFactory.getLogger(HttpResponseHandler.class);

    public HttpResponseHandler() {
        httpResponse = new CustomHttpResponse();
    }

    public CustomHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void writeResponseHeader(CustomHttpRequest request) {
        Map<String, String> requestLineMap = request.getRequestLineMap();
        CustomResponseBody body = httpResponse.getBody();


        httpResponse.writeHeader(String.format("%s %s %d \r\n", requestLineMap.get("http"),
                httpResponse.getHttpStatus().name(), httpResponse.getHttpStatus().code));
        httpResponse.writeHeader(String.format("Content-Type: %s;charset=utf-8\r\n",
                request.getHeaderAttr("Accept").split(",")[0]));
        httpResponse.writeHeader("Content-Length: " + body.getBodyContent().length + "\r\n");
        httpResponse.writeHeader("\r\n");
    }
}
