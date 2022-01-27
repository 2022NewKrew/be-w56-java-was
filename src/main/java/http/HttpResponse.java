package http;

import enums.HttpMethod;
import enums.HttpProtocol;
import enums.HttpStatusCode;
import util.HttpRequestUtils;
import util.HttpResponseUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponse {

    private HttpProtocol protocol;
    private HttpStatusCode statusCode;
    private byte[] body;
    private String responseContentType;
    private String responseDataPath;

    public byte[] getBody() {
        return body;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public HttpResponse(HttpRequest httpRequest) throws IOException {
        protocol = httpRequest.getProtocol();
        setResponseDataPath(httpRequest);
        responseContentType = HttpResponseUtils.contentTypeFromPath(responseDataPath);
        if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            body = Files.readAllBytes(new File("./webapp" + responseDataPath).toPath());
        }
    }

    private void setResponseDataPath(HttpRequest httpRequest) {
        String requestUrl = httpRequest.getUrl();
        String url = HttpRequestUtils.parseUrl(requestUrl).get("url");
        responseDataPath = url;
    }

}
