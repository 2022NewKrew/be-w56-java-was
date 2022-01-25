package mapper;

import Controller.Controller;
import Controller.RootController;
import Controller.StaticController;
import Controller.ErrorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UrlMapper {
    private final Map<String, Controller> controllerMap;
    private static final Logger log = LoggerFactory.getLogger(UrlMapper.class);
    private final Controller rootController;
    private final Controller staticController;
    private final Controller errorController;

    public UrlMapper(){
        controllerMap = new LinkedHashMap<String, Controller>();
        rootController = new RootController();
        staticController = new StaticController();
        errorController = new ErrorController();

        controllerMap.put("/js", staticController);
        controllerMap.put("/css", staticController);
        controllerMap.put("/fonts", staticController);
        controllerMap.put("/images", staticController);
        controllerMap.put("/favicon.ico", staticController);
        controllerMap.put("/", rootController);
        controllerMap.put("/error", errorController);
    }

    public Map<String, Object> mappingResult(HttpRequest httpRequest){
        String method = httpRequest.getMethod();
        String url = httpRequest.getUrl();
        Map<String, String> message = httpRequest.getData();

        return controllerMap.get(createMappingUrl(url)).run(method, url, message);
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
