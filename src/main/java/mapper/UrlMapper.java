package mapper;

import Controller.Controller;
import Controller.RootController;
import Controller.StaticController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class UrlMapper {
    private final Map<String, Controller> controllerMap;
    private static final Logger log = LoggerFactory.getLogger(UrlMapper.class);
    private final Controller rootController;
    private final Controller staticController;

    public UrlMapper(){
        controllerMap = new LinkedHashMap<String, Controller>();
        rootController = new RootController();
        staticController = new StaticController();

        controllerMap.put("/js", staticController);
        controllerMap.put("/css", staticController);
        controllerMap.put("/fonts", staticController);
        controllerMap.put("/images", staticController);
        controllerMap.put("/favicon.ico", staticController);
        controllerMap.put("/index.html", rootController);
    }

    public Map<String, Object> mappingResult(String method, String url, Map<String, String> message){
        return controllerMap.get(createMappingUrl(url)).run(method, url, message);
    }

    private String createMappingUrl(String url){
        for (Map.Entry<String, Controller> map: controllerMap.entrySet()){
            String regUrl = map.getKey();

            if(url.matches("^" + regUrl + ".*"))
                return regUrl;
        }

        return "/index.html";
    }
}
