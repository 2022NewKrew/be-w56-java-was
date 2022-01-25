package webserver.handler.outbound;

import webserver.response.HttpResponse;

/**
 * Singleton
 */
public class ContentLengthHandler extends OutboundHandler {

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final ContentLengthHandler INSTANCE = new ContentLengthHandler();

    private ContentLengthHandler() {
    }

    public static ContentLengthHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void handle(HttpResponse response) {
        int contentLength = response.getLengthOfBodyContent();
        response.setHeaderIfAbsent(CONTENT_LENGTH, new String[]{String.valueOf(contentLength)});
    }
}
