package webserver.controller;

import DTO.ModelAndView;
import DTO.RequestHeader;
import DTO.ResponseHeader;
import Service.UserService;
import db.DataBase;
import model.User;
import java.util.Map;

public class UserListController implements Controller {

    static {
        Controller showListController = new ShowListController();
        controllers.add(showListController);
    }

    static class ShowListController implements Controller {
        final private String url = "/user/list";

        @Override
        public ModelAndView getResponse(RequestHeader requestHeader, ResponseHeader responseHeader) {
            log.info("DB : {}", DataBase.printUserIdPw());

            if (UserService.checkLogin(requestHeader)) { // if user is in login status
                return new ModelAndView("/user/list.html");
            }

            return new ModelAndView("redirect:/user/login.html");
        }

        @Override
        public String getUrl() {
            return url;
        }
    }
}
