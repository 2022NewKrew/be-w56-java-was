package webserver.handler.outbound;

import webserver.response.HttpResponse;

/**
 * Singleton
 */
public class ContentLengthHandler extends OutboundHandler {

    private static final String CONTENT_LENGTH = "Content-Length";

    private static ContentLengthHandler INSTANCE;

    private ContentLengthHandler() {
    }

    public static ContentLengthHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContentLengthHandler();
        }
        return INSTANCE;
    }

    @Override
    public void handle(HttpResponse response) {
        int contentLength = response.getLengthOfBodyContent();
        response.setHeaderIfAbsent(CONTENT_LENGTH, new String[]{String.valueOf(contentLength)});
    }
}
