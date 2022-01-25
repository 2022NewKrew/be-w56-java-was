package webserver.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpMethod;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseHeader;
import webserver.response.HttpResponseStatusLine;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class WebService {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    public void doService(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        HttpMethod httpMethod = httpRequest.getHttpRequestStartLine().getMethod();

        switch (httpMethod) {
            case GET:
                doGet(httpRequest, httpResponse);
                break;
            default:
                break;
        }
    }

    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String targetUri = httpRequest.getHttpRequestStartLine().getTargetUri();

        byte[] body = Files.readAllBytes(new File("./webapp" + targetUri).toPath());

        HttpResponseStatusLine httpResponseStatusLine = httpResponse.getHttpResponseStatusLine();

        httpResponseStatusLine.setVersion(httpRequest.getHttpRequestStartLine().getVersion());
        httpResponseStatusLine.setCode(200);
        httpResponseStatusLine.setMessage("OK");

        HttpResponseHeader httpResponseHeader = httpResponse.getHttpResponseHeader();

        httpResponseHeader.setConnection(httpRequest.getHttpRequestHeader().getConnection());
        httpResponseHeader.setContentType(httpRequest.getHttpRequestHeader().getContentType());
        httpResponseHeader.setContentLength((long) body.length);

        httpResponse.setBody(body);

        responseHeader(httpResponse);
        responseBody(httpResponse);
    }

    private void responseHeader(HttpResponse httpResponse) {
        try {
            DataOutputStream dos = httpResponse.getDos();

            dos.writeBytes(httpResponse.toString());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(HttpResponse httpResponse) {
        try {
            DataOutputStream dos = httpResponse.getDos();
            byte[] body = httpResponse.getBody();

            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
