package mapper;

import controller.Controller;
import controller.RootController;
import controller.StaticController;
import controller.ErrorController;
import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;

import java.util.LinkedHashMap;
import java.util.Map;
import static mapper.MappingConst.*;

public class UrlMapper {
    private final Map<String, Controller> controllerMap;
    private static final Logger log = LoggerFactory.getLogger(UrlMapper.class);
    private final Controller rootController;
    private final Controller staticController;
    private final Controller errorController;
    private final Controller userController;

    public UrlMapper(){
        controllerMap = new LinkedHashMap<String, Controller>();
        rootController = new RootController();
        staticController = new StaticController();
        errorController = new ErrorController();
        userController = new UserController();

        controllerMap.put(JS, staticController);
        controllerMap.put(CSS, staticController);
        controllerMap.put(FONT, staticController);
        controllerMap.put(IMAGE, staticController);
        controllerMap.put(ICON, staticController);
        controllerMap.put(ERROR, errorController);
        controllerMap.put(USER, userController);

        controllerMap.put(ROOT, rootController);
    }

    public ResponseSendDataModel mappingResult(HttpRequest httpRequest){

        return controllerMap.get(createMappingUrl(httpRequest.getUrl())).run(httpRequest);
    }

    private String createMappingUrl(String url){
        url = url.split("\\?")[0];

        for (Map.Entry<String, Controller> map: controllerMap.entrySet()){
            String regUrl = map.getKey();

            if(url.matches("^" + regUrl + ".*"))
                return regUrl;
        }

        return "/error";
    }
}
