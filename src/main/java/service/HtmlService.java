package service;

import db.DataBase;
import model.User;
import util.HtmlUtils;

import java.util.ArrayList;
import java.util.List;

public class HtmlService {

    public void makeUserList() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"kr\">\n");
        writeHead(sb);
        sb.append("<body>\n");
        writeNavBar(sb);
        writeUserList(sb);
        writeJs(sb);
        sb.append("</body>\n");
        sb.append("</html>\n");
        String path = "./webapp/users/list.html";
        HtmlUtils.writeFile(path, sb.toString());
    }

    private void writeHead(StringBuilder sb) {
        sb.append("<head>\n");
        sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
        sb.append("<meta charset=\"utf-8\">\n");
        sb.append("<title>SLiPP Java Web Programming</title>\n");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n");
        sb.append("<link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">\n");
        sb.append("<!--[if lt IE 9]>\n");
        sb.append("<script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n");
        sb.append("<![endif]-->\n");
        sb.append("<link href=\"../css/styles.css\" rel=\"stylesheet\">\n");
        sb.append("</head>\n");
    }

    private void writeNavBar(StringBuilder sb) {
        sb.append("<nav class=\"navbar navbar-fixed-top header\">\n");
        sb.append("<div class=\"col-md-12\">\n");
        sb.append("<div class=\"navbar-header\">\n\n");
        sb.append("<a href=\"../index.html\" class=\"navbar-brand\">SLiPP</a>\n");
        sb.append("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n");
        sb.append("<i class=\"glyphicon glyphicon-search\"></i>\n");
        sb.append("</button>\n\n");
        sb.append("</div>\n");
        sb.append("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n");
        sb.append("<form class=\"navbar-form pull-left\">\n");
        sb.append("<div class=\"input-group\" style=\"max-width:470px;\">\n");
        sb.append("<input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n");
        sb.append("<div class=\"input-group-btn\">\n");
        sb.append("<button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append("</form>\n");
        sb.append("<ul class=\"nav navbar-nav navbar-right\">\n");
        sb.append("<li>\n");
        sb.append("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n");
        sb.append("<ul class=\"dropdown-menu\">\n");
        sb.append("<li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n");
        sb.append("<li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n");
        sb.append("</ul>\n");
        sb.append("</li>\n");
        sb.append("<li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n");
        sb.append("</ul>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append("</nav>\n");
        sb.append("<div class=\"navbar navbar-default\" id=\"subnav\">\n");
        sb.append("<div class=\"col-md-12\">\n");
        sb.append("<div class=\"navbar-header\">\n");
        sb.append("<a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n");
        sb.append("<ul class=\"nav dropdown-menu\">\n");
        sb.append("<li><a href=\"../user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n");
        sb.append("<li class=\"nav-divider\"></li>\n");
        sb.append("<li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n");
        sb.append("</ul>\n\n");
        sb.append("<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n");
        sb.append("<span class=\"sr-only\">Toggle navigation</span>\n");
        sb.append("<span class=\"icon-bar\"></span>\n");
        sb.append("<span class=\"icon-bar\"></span>\n");
        sb.append("<span class=\"icon-bar\"></span>\n");
        sb.append("</button>\n");
        sb.append("</div>\n");
        sb.append("<div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n");
        sb.append("<ul class=\"nav navbar-nav navbar-right\">\n");
        sb.append("<li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n");
        sb.append("<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>\n");
        sb.append("<li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>\n");
        sb.append("<li><a href=\"#\" role=\"button\">로그아웃</a></li>\n");
        sb.append("<li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n");
        sb.append("</ul>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
    }

    private void writeUserList(StringBuilder sb) {
        List<User> userList = new ArrayList<>(DataBase.findAll()) ;
        sb.append("<div class=\"container\" id=\"main\">\n");
        sb.append("<div class=\"col-md-10 col-md-offset-1\">\n");
        sb.append("<div class=\"panel panel-default\">\n");
        sb.append("<table class=\"table table-hover\">\n");
        sb.append("<thead>\n");
        sb.append("<tr>\n");
        sb.append("<th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>\n");
        sb.append("</tr>\n");
        sb.append("</thead>\n");
        sb.append("<tbody>\n");
        int index = 1;
        for(User user : userList) {
            sb.append(String.format("<tr>\n<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n</tr>\n", index, user.getUserId(), user.getName(), user.getEmail()));
            System.out.println(user.getName());
            index++;
        }
        sb.append("</tbody>\n");
        sb.append("</table>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
        sb.append("</div>\n");
    }

    private void writeJs(StringBuilder sb) {
        sb.append("<script src=\"../js/jquery-2.2.0.min.js\"></script>\n");
        sb.append("<script src=\"../js/bootstrap.min.js\"></script>\n");
        sb.append("<script src=\"../js/scripts.js\"></script>\n");
    }

}
