package util;

import controller.Controller;
import controller.UserController;

import java.util.*;

public class HandlerMapping {
    private static final int PATH_METHOD_SPLIT_INDEX = 2;

    private final Map<String, Controller> controllerMap;
    private final Set<String> redirectSet;

    private static final HandlerMapping handlerMapping = new HandlerMapping();

    private HandlerMapping() {
        Map<String, Controller> tempControllerMap = new HashMap<>();
        Set<String> tempRedirectSet = new HashSet<>();

        UserController userController = new UserController();
        tempControllerMap.put("/user/create", userController);
        tempRedirectSet.add("/user/create");

        controllerMap = Collections.unmodifiableMap(tempControllerMap);
        redirectSet = Collections.unmodifiableSet(tempRedirectSet);
    }

    public static HandlerMapping getInstance() {
        return handlerMapping;
    }

    public RedirectPair runMethod(String url, Map<String, String> infoMap) {
        Optional<Controller> controllerOptional = getController(url);

        if (controllerOptional.isEmpty())
            return new RedirectPair(url,false);

        String methodName = url.split("/")[PATH_METHOD_SPLIT_INDEX];
        if (methodName.equals("create"))
            return new RedirectPair(controllerOptional.get().create(infoMap),getIsRedirect(url));

        return new RedirectPair(url,false);
    }

    public Optional<Controller> getController(String url) {
        Optional<Controller> controllerOptional;
        if (controllerMap.containsKey(url))
            controllerOptional = Optional.of(controllerMap.get(url));
        else
            controllerOptional = Optional.empty();
        return controllerOptional;
    }

    public boolean getIsRedirect(String url){
        return redirectSet.contains(url);
    }
}
