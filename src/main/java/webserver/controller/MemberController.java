package webserver.controller;

import DTO.ModelAndView;
import DTO.RequestHeader;
import DTO.ResponseHeader;
import Service.UserService;
import db.DataBase;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class MemberController implements Controller {

    static {
        Controller joinController = new JoinController();
        Controller loginController = new LoginController();
        controllers.add(joinController);
        controllers.add(loginController);
    }

    // @ RequestMapping(CREATE_USER)
    static class JoinController implements Controller{
        final private String url = "/user/create";

        @Override
        public ModelAndView getResponse(RequestHeader requestHeader, ResponseHeader responseHeader){
            Map<String, String> requestParam = requestHeader.getBody();
            User user = UserService.join(requestParam);

            log.info("Create New User : {}", user);
            log.info("DB Changed: {}", DataBase.printUserIdPw());

            return new ModelAndView("redirect:/index.html");
        }

        @Override
        public String getUrl() {
            return url;
        }
    }

    // @ RequestMapping(LOGIN)
    static class LoginController implements Controller{
        final private String url = "/user/login";
        final static private Map<String, String> cookieOptions = new HashMap<>();
        static {
            cookieOptions.put("Path","/");
        }


        @Override
        public ModelAndView getResponse(RequestHeader requestHeader, ResponseHeader responseHeader){
            log.info("Print DB : {}", DataBase.printUserIdPw());

            try{
                loginSuccess(requestHeader, responseHeader);
                return new ModelAndView("/index.html");

            }catch (IllegalArgumentException e){
                log.debug(e.getMessage());
                loginFail(requestHeader, responseHeader);
                return new ModelAndView("/user/login_failed.html");
            }

        }

        private void loginSuccess(RequestHeader requestHeader, ResponseHeader responseHeader){

            Map<String, String> requestParam = requestHeader.getBody();
            User user = UserService.login(requestParam);
            log.info("User Login : {}", user);

            responseHeader.setCookie("logined","true", cookieOptions);
            requestHeader.setCookie("logined","true");

            // todo : 1. create sessionID in db
            // 2. return session id in cookie
            // 3. store session id in db.
        }

        private void loginFail(RequestHeader requestHeader, ResponseHeader responseHeader){

            log.info("User Login Failed");

            responseHeader.setCookie("logined","false", cookieOptions);
        }


        @Override
        public String getUrl() {
            return url;
        }
    }

}
