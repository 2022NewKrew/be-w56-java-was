package controller;

import dto.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import viewresolver.GetViewResolver;

import java.io.DataOutputStream;

public class DefaultController implements Controller {

    private static final Logger log;
    private static final DefaultController INSTANCE;
    private static final GetViewResolver GET_VIEW_RESOLVER;

    static {
        log = LoggerFactory.getLogger(DefaultController.class);
        INSTANCE = new DefaultController();
        GET_VIEW_RESOLVER = GetViewResolver.getInstance();
    }

    private DefaultController() {}

    public static DefaultController getInstance() {
        return INSTANCE;
    }

    @Override
    public void handleRequest(RequestInfo requestInfo, DataOutputStream dos) {
        doGet(requestInfo, dos);
    }

    @Override
    public void doGet(RequestInfo requestInfo, DataOutputStream dos) {
        GET_VIEW_RESOLVER.response(requestInfo, dos);
    }
}
