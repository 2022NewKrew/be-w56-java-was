package util;

import controller.Controller;
import controller.UserController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapping {
    private static final int PATH_METHOD_SPLIT_INDEX = 2;

    private final Map<String, Controller> mappings;

    private static final HandlerMapping handlerMapping = new HandlerMapping();

    private HandlerMapping() {
        Map<String, Controller> tempMappings = new HashMap<>();
        UserController userController = new UserController();
        tempMappings.put("/user/create", userController);
        mappings = Collections.unmodifiableMap(tempMappings);
    }

    public static HandlerMapping getInstance() {
        return handlerMapping;
    }

    public Optional<Controller> getController(String url) {
        Optional<Controller> controllerOptional;
        if (mappings.containsKey(url))
            controllerOptional = Optional.of(mappings.get(url));
        else
            controllerOptional = Optional.empty();
        return controllerOptional;
    }

    public String runMethod(String url, Map<String, String> infoMap) {
        Optional<Controller> controllerOptional = getController(url);

        if (controllerOptional.isEmpty())
            return url;

        String methodName = url.split("/")[PATH_METHOD_SPLIT_INDEX];
        if (methodName.equals("create"))
            return controllerOptional.get().create(infoMap);

        return url;
    }
}
