package webserver.dispatcher;

import webserver.response.HttpResponse;

// todo
/**
 * Singleton
 */
public class DynamicDispatcher extends Dispatcher {

    private static DynamicDispatcher INSTANCE;

    private DynamicDispatcher() {
    }

    public static DynamicDispatcher getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DynamicDispatcher();
        }
        return INSTANCE;
    }

    @Override
    protected HttpResponse createResponse() {
        return null;
    }

}
