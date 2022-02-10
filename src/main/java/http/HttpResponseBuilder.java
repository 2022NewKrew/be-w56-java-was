package http;

import enums.HttpMethod;
import enums.HttpStatusCode;
import util.HttpRequestUtils;
import util.HttpResponseUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class HttpResponseBuilder {

    private HttpRequest httpRequest;
    private HttpResponse httpResponse = new HttpResponse();

    public HttpResponseBuilder(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void build(ResponseBuildInfo responseBuildInfo) throws IOException {
        httpResponse.setProtocol(httpRequest.getProtocol());
        for (String key : responseBuildInfo.getCookie().keySet()) {
            httpResponse.addCookie(key, responseBuildInfo.getCookie().get(key));
        }
        if (responseBuildInfo.isRedirect()) {
            httpResponse.setStatusCode(HttpStatusCode._302);
            httpResponse.setRedirectUrl(responseBuildInfo.getPath());
            return;
        }
        httpResponse.setStatusCode(HttpStatusCode._200);
        httpResponse.setResponseDataPath(responseBuildInfo.getPath());
        httpResponse.setResponseContentType(HttpResponseUtils.contentTypeFromPath(httpResponse.getResponseDataPath()));
        buildBody();
    }

    private void buildBody() throws IOException {
        httpResponse.setBody(Files.readAllBytes(new File("./webapp" + httpResponse.getResponseDataPath()).toPath()));
    }

}
