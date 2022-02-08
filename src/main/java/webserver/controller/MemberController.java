package webserver.controller;

import DTO.ModelAndView;
import DTO.RequestHeader;
import DTO.ResponseHeader;
import Service.UserService;
import db.DataBase;
import model.User;

import java.util.Map;

public class MemberController implements Controller {

    static {
        Controller joinController = new JoinController();
        controllers.add(joinController);
    }

    // @ RequestMapping(CREATE_USER)
    static class JoinController implements Controller{
        final private String url = "/user/create";

        @Override
        public ModelAndView getResponse(RequestHeader requestHeader, ResponseHeader responseHeader){
            Map<String, String> requestParam = requestHeader.getBody();
            String requestUrl= requestHeader.getRequestUrl();
            User user = UserService.join(requestParam);
            log.info("Create New User : {}", user);
            log.info("Url Changed: {}", requestUrl);
            log.info("DB Changed: {}", DataBase.allIdsToString());


            return new ModelAndView("redirect:/index.html");
        }

        @Override
        public String getUrl() {
            return url;
        }
    }

}
