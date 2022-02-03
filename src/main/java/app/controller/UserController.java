package app.controller;

import app.db.DataBase;
import app.model.User;
import http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Cookie;
import webserver.http.RequestEntity;
import webserver.http.RequestParams;
import webserver.http.ResponseEntity;
import webserver.http.request.EmptyBody;
import webserver.http.request.StringBody;
import webserver.http.response.Redirect;
import webserver.http.response.View;
import webserver.processor.handler.controller.Controller;
import webserver.processor.handler.controller.Request;

import java.util.*;

public class UserController implements Controller {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Request(method = HttpMethod.GET, value = "/user/list")
    public ResponseEntity<View> getUserList(RequestEntity<EmptyBody> requestEntity) {
        List<Cookie> cookies = requestEntity.getCookies();
        checkUserLogin(cookies);
        Collection<User> allUsers = DataBase.findAll();
        String body = getUserListView(allUsers);
        return new ResponseEntity<>(new HttpHeaders(Map.of()), List.of(), StatusCode.OK, View.body(body));
    }

    @Request(method = HttpMethod.POST, value = "/user/create")
    public ResponseEntity<Redirect> createUserRequest(RequestEntity<StringBody> requestEntity) {
        RequestParams requestParams = requestEntity.getRequestParams();
        User user = createUser(requestParams);
        DataBase.addUser(user);
        HttpHeaders headers = new HttpHeaders(Map.of("Location", "/"));
        logger.info("User Created Id : {}, password : {}, name : {}, email : {}", user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        return new ResponseEntity<>(headers, List.of(), StatusCode.FOUND, Redirect.from("/"));
    }

    @Request(method = HttpMethod.POST, value = "/user/login")
    public ResponseEntity<Redirect> loginUser(RequestEntity<EmptyBody> requestEntity) {
        RequestParams requestParams = requestEntity.getRequestParams();
        String userId = requestParams.getParam("userId");
        String password = requestParams.getParam("password");
        User user = DataBase.findUserById(userId);
        HttpHeaders headers = new HttpHeaders(new HashMap<>());
        if(user.getPassword().equals(password)) {
            Cookie cookie = new Cookie("logined", "true");
            return new ResponseEntity<>(headers, List.of(cookie), StatusCode.FOUND, Redirect.from("/"));
        }
        return new ResponseEntity<>(headers, new ArrayList<>(), StatusCode.FOUND, Redirect.from("/user/login_failed.html"));
    }

    private void checkUserLogin(List<Cookie> cookies) {
        boolean isLogin = cookies.stream().anyMatch(cookie -> cookie.getKey().equals("logined") && cookie.getValue().equals("true"));
        if(!isLogin) {
            throw new AnonymousUserException("");
        }
    }

    private User createUser(RequestParams requestParams) {
        String userId = requestParams.getParam("userId");
        String password = requestParams.getParam("password");
        String name = requestParams.getParam("name");
        String email = requestParams.getParam("email");
        return new User(userId, password, name, email);
    }

    private String getUserListView(Collection<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n" +
                "<html lang=\"kr\">\n" +
                "<head>\n" +
                "    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>SLiPP Java Web Programming</title>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
                "    <link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "    <!--[if lt IE 9]>\n" +
                "    <script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n" +
                "    <![endif]-->\n" +
                "    <link href=\"../css/styles.css\" rel=\"stylesheet\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<nav class=\"navbar navbar-fixed-top header\">\n" +
                "    <div class=\"col-md-12\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "\n" +
                "            <a href=\"../index.html\" class=\"navbar-brand\">SLiPP</a>\n" +
                "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n" +
                "                <i class=\"glyphicon glyphicon-search\"></i>\n" +
                "            </button>\n" +
                "\n" +
                "        </div>\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n" +
                "            <form class=\"navbar-form pull-left\">\n" +
                "                <div class=\"input-group\" style=\"max-width:470px;\">\n" +
                "                    <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n" +
                "                    <div class=\"input-group-btn\">\n" +
                "                        <button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </form>\n" +
                "            <ul class=\"nav navbar-nav navbar-right\">\n" +
                "                <li>\n" +
                "                    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n" +
                "                    <ul class=\"dropdown-menu\">\n" +
                "                        <li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n" +
                "                        <li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n" +
                "                    </ul>\n" +
                "                </li>\n" +
                "                <li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</nav>\n" +
                "<div class=\"navbar navbar-default\" id=\"subnav\">\n" +
                "    <div class=\"col-md-12\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "            <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n" +
                "            <ul class=\"nav dropdown-menu\">\n" +
                "                <li><a href=\"../user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n" +
                "                <li class=\"nav-divider\"></li>\n" +
                "                <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n" +
                "            </ul>\n" +
                "            \n" +
                "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n" +
                "            \t<span class=\"sr-only\">Toggle navigation</span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            \t<span class=\"icon-bar\"></span>\n" +
                "            </button>            \n" +
                "        </div>\n" +
                "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n" +
                "            <ul class=\"nav navbar-nav navbar-right\">\n" +
                "                <li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n" +
                "                <li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>\n" +
                "                <li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>\n" +
                "                <li><a href=\"#\" role=\"button\">로그아웃</a></li>\n" +
                "                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<div class=\"container\" id=\"main\">\n" +
                "   <div class=\"col-md-10 col-md-offset-1\">\n" +
                "      <div class=\"panel panel-default\">\n" +
                "          <table class=\"table table-hover\">\n" +
                "              <thead>\n" +
                "                <tr>\n" +
                "                    <th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>\n" +
                "                </tr>\n" +
                "              </thead>\n" +
                "              <tbody>\n");

        int count = 1;
        for(User user : users) {
            sb.append(String.format(
                    "<tr>\n" +
                        "<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
                    "</tr>", count++, user.getUserId(), user.getName(), user.getEmail()));
        }
        sb.append("</tbody>\n" +
                "          </table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "\n" +
                "<!-- script references -->\n" +
                "<script src=\"../js/jquery-2.2.0.min.js\"></script>\n" +
                "<script src=\"../js/bootstrap.min.js\"></script>\n" +
                "<script src=\"../js/scripts.js\"></script>\n" +
                "\t</body>\n" +
                "</html>");
        return sb.toString();
    }
}
