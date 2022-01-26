package webserver.dispatcher.sta;

import webserver.dispatcher.Dispatcher;
import webserver.request.HttpRequestUri;
import webserver.request.RequestContext;
import webserver.response.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton
 */
public class StaticDispatcher extends Dispatcher {

    private static final String DEFAULT_LOCATION_OF_RESOURCES = "./webapp";

    private static final StaticDispatcher INSTANCE = new StaticDispatcher();

    private StaticDispatcher() {
    }

    public static StaticDispatcher getInstance() {
        return INSTANCE;
    }

    @Override
    protected HttpResponse createResponse() {
        HttpRequestUri requestUri = RequestContext.getInstance().getHttpRequest().getUri();
        try {
            byte[] fileToBytes = readFiles(requestUri.getUrl());
            return getResponseForFile(fileToBytes);
        } catch (IOException e) {
            return getResponseForNotFound();
        }
    }

    private byte[] readFiles(String uri) throws IOException {
        return Files.readAllBytes(new File(DEFAULT_LOCATION_OF_RESOURCES + uri).toPath());
    }

    private HttpResponse getResponseForFile(byte[] fileToBytes) {
        HttpResponseLine responseLine = new HttpResponseLine(HttpStatus.OK);

        Map<String, String[]> headers = new HashMap<>();
        HttpResponseHeader responseHeader = new HttpResponseHeader(headers);

        HttpResponseBody responseBody = new HttpResponseBody(fileToBytes);

        return new HttpResponse(responseLine, responseHeader, responseBody);
    }

    private HttpResponse getResponseForNotFound() {
        HttpResponseLine responseLine = new HttpResponseLine(HttpStatus.NOT_FOUND);

        Map<String, String[]> headers = new HashMap<>();
        headers.put(HttpResponseHeader.KEY_FOR_CONTENT_TYPE, new String[]{ContentType.TEXT_HTML.getValue()});
        HttpResponseHeader responseHeader = new HttpResponseHeader(headers);

        HttpResponseBody responseBody = new HttpResponseBody(new byte[]{});

        return new HttpResponse(responseLine, responseHeader, responseBody);
    }
}
