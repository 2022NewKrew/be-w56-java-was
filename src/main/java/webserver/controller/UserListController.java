package webserver.controller;

import DTO.ModelAndView;
import DTO.RequestHeader;
import DTO.ResponseHeader;
import Service.UserService;
import db.DataBase;

public class UserListController implements Controller {

    static {
        Controller showListController = new ShowListController();
        controllers.add(showListController);
    }

    static class ShowListController implements Controller {
        final private String url = "/user/list";

        @Override
        public ModelAndView getResponse(RequestHeader requestHeader, ResponseHeader responseHeader) {
            log.info("userlist controller DB : {}", DataBase.printUserIdPw());
            if (UserService.checkLogin(requestHeader)) { // if user is in login status

                ModelAndView mav = new ModelAndView("/user/list.html");
                mav.addObject("users", DataBase.getUserList());
                return mav;
            }

            return new ModelAndView("redirect:/user/login.html");
        }

        @Override
        public String getUrl() {
            return url;
        }
    }
}
