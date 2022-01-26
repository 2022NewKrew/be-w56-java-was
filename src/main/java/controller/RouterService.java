package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RouterService {
    private final Map<String, Router> getRouter;
    private final Map<String, Router> postRouter;

    public RouterService() {
        getRouter = new HashMap<>();
        postRouter = new HashMap<>();
    }

    public void get(String path, Router router) {
        getRouter.put(path, router);
    }

    public void post(String path, Router router) {
        postRouter.put(path, router);
    }

    public Optional<Router> returnUrl(String method, String url) {
        if (method.equals("GET")) {
            return Optional.ofNullable(getRouter.get(url));
        }

        if (method.equals("POST")) {
            return Optional.ofNullable(postRouter.get(url));
        }

        return Optional.empty();
    }
}
