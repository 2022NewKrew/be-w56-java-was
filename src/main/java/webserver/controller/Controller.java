package webserver.controller;

import DTO.ModelAndView;
import DTO.RequestHeader;
import DTO.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Controller {
    Logger log = LoggerFactory.getLogger(Controller.class);
    List<Controller> controllers = new ArrayList<>();

    default ModelAndView getResponse(RequestHeader requestHeader, ResponseHeader responseHeader){
        return null;
    }

    default String getUrl(){
        return "";
    }

    default void map(Map<String, Controller> controllerList){
        for(Controller controller : controllers){
            String strUrl = controller.getUrl();
            log.info("-- {} added to controller map -- ", strUrl);
            controllerList.put(strUrl, controller);
        }
    }
}
