package webserver.controller;

import DTO.ModelAndView;
import DTO.RequestHeader;
import DTO.ResponseHeader;
import Service.UserService;
import db.DataBase;
import db.SessionDb;
import model.User;
import webserver.Session;

import java.util.HashMap;
import java.util.Map;

public class MemberController implements Controller {

    static {
        Controller joinController = new JoinController();
        Controller loginController = new LoginController();
        Controller logoutController = new LogoutController();
        controllers.add(joinController);
        controllers.add(loginController);
        controllers.add(logoutController);
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
                return new ModelAndView("redirect:/index.html");

            }catch (IllegalArgumentException e){
                log.debug(e.getMessage());
                loginFail(requestHeader, responseHeader);
                return new ModelAndView("/user/login_failed.html");
            }

        }

        private void loginSuccess(RequestHeader requestHeader, ResponseHeader responseHeader) throws IllegalArgumentException{

            Map<String, String> requestParam = requestHeader.getBody();
            String sessionId = UserService.login(requestParam);
            log.info("User Login : {}", sessionId);

            responseHeader.setCookie("logined","true", cookieOptions);
            requestHeader.setCookie("logined","true");

            // 1. create sessionID in db
            // 2. return session id in cookie
            // 3. store session id in db.
            responseHeader.setCookie("SESSIONID", sessionId, cookieOptions);
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

    static class LogoutController implements Controller{
        final private String url = "/user/logout";
        final static private Map<String, String> cookieOptions = new HashMap<>();
        static {
            cookieOptions.put("Path","/");
        }


        @Override
        public ModelAndView getResponse(RequestHeader requestHeader, ResponseHeader responseHeader){
            log.info("Print DB : {}", DataBase.printUserIdPw());

            try{
                logout(requestHeader, responseHeader);
                return new ModelAndView("redirect:/index.html");

            }catch (Exception e){
                log.debug(e.getMessage());
                return new ModelAndView("redirect:/index.html");
            }

        }

        private void logout(RequestHeader requestHeader, ResponseHeader responseHeader){
            String sid = UserService.logout(); // session logout;

            log.info("User LogOut ");

            responseHeader.removeCookie("SESSIONID", sid);
            requestHeader.removeCookie("SESSIONID", sid);

            responseHeader.setCookie("logined","false", cookieOptions);
            requestHeader.setCookie("logined","false");


        }

        @Override
        public String getUrl() {
            return url;
        }

    }

}
