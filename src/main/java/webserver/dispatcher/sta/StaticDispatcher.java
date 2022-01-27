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
import java.util.Optional;

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
    protected HttpResponse processRequest() {
        HttpRequestUri requestUri = RequestContext.getInstance().getHttpRequest().getUri();
        try {
            byte[] fileToBytes = readFiles(requestUri.getUrl());
            return getResponseForFile(fileToBytes);
        } catch (IOException e) {
            return getResponseForNotFound();
        }
    }

    private byte[] readFiles(String url) throws IOException {
        ResourceCache cache = ResourceCache.getInstance();
        Optional<byte[]> fileBytesOptional = cache.getFile(url);
        if (fileBytesOptional.isEmpty()) {
            byte[] fileToBytes = Files.readAllBytes(new File(DEFAULT_LOCATION_OF_RESOURCES + url).toPath());
            return cache.addFile(url, fileToBytes);
        }
        return fileBytesOptional.get();
    }

    private HttpResponse getResponseForFile(byte[] fileToBytes) {
        HttpResponse response = ResponseContext.getInstance().getHttpResponse();
        setHttpStatusForResponse(response, HttpStatus.OK);

        response.setResponseBody(fileToBytes);

        return response;
    }

    private HttpResponse getResponseForNotFound() {
        HttpResponse response = ResponseContext.getInstance().getHttpResponse();
        setHttpStatusForResponse(response, HttpStatus.NOT_FOUND);

        response.setHeaderIfAbsent(HttpResponseHeader.KEY_FOR_CONTENT_TYPE, new String[]{ContentType.TEXT_HTML.getValue()});

        return response;
    }

    private void setHttpStatusForResponse(HttpResponse response, HttpStatus status) {
        response.setHttpStatus(status);
    }
}
