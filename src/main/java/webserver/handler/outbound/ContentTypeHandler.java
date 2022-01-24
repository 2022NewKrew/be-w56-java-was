package webserver.handler.outbound;

import webserver.request.HttpRequest;
import webserver.request.HttpRequestUri;
import webserver.request.RequestContext;
import webserver.response.ContentType;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseHeader;

/**
 * Singleton
 */
public class ContentTypeHandler extends OutboundHandler {

    private static ContentTypeHandler INSTANCE;

    private ContentTypeHandler() {
    }

    public static ContentTypeHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContentTypeHandler();
        }
        return INSTANCE;
    }

    @Override
    public void handle(HttpResponse response) {
        HttpRequest httpRequest = RequestContext.getInstance().getHttpRequest();
        HttpRequestUri uri = httpRequest.getUri();
        if (uri.isForStaticContent()) {
            setContentTypeForStaticContent(uri, response);
            return;
        }
        setContentTypeForDynamicContent(uri, response);
    }

    private void setContentTypeForStaticContent(HttpRequestUri uri, HttpResponse response) {
        String contentType = getContentTypeForStaticContent(uri.getUrl());
        response.setHeaderIfAbsent(HttpResponseHeader.KEY_FOR_CONTENT_TYPE, new String[]{contentType});
    }

    private String getContentTypeForStaticContent(String url) {
        int indexForExtension = url.lastIndexOf('.');
        String extension = url.substring(indexForExtension + 1);
        return ContentType.getContentTypeByExtension(extension).getValue();
    }

    private void setContentTypeForDynamicContent(HttpRequestUri uri, HttpResponse response) {
        String contentType = getContentTypeForDynamicContent();
        response.setHeaderIfAbsent(HttpResponseHeader.KEY_FOR_CONTENT_TYPE, new String[]{contentType});
    }

    //todo
    private String getContentTypeForDynamicContent() {
        return null;
    }
}
