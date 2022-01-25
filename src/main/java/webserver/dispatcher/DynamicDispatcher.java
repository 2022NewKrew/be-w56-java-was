package webserver.dispatcher;

import webserver.response.HttpResponse;

// todo
/**
 * Singleton
 */
public class DynamicDispatcher extends Dispatcher {

    private static final DynamicDispatcher INSTANCE = new DynamicDispatcher();

    private DynamicDispatcher() {
    }

    public static DynamicDispatcher getInstance() {
        return INSTANCE;
    }

    @Override
    protected HttpResponse createResponse() {
        return null;
    }

}
