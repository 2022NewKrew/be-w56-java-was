package exception;

import model.ResponseHeader;
import util.HtmlResponseHeader;
import util.Links;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExceptionHandler {
    private static final String ERROR_PAGE_BODY = "<!DOCTYPE html>\n" +
            "<html lang=\"kr\">\n" +
            "<head>\n" +
            "    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <title>SLiPP Java Web Programming</title>\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
            "    <link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
            "    <!--[if lt IE 9]>\n" +
            "    <script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n" +
            "    <![endif]-->\n" +
            "    <link href=\"css/styles.css\" rel=\"stylesheet\">\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "<nav class=\"navbar navbar-fixed-top header\">\n" +
            "    <div class=\"col-md-12\">\n" +
            "        <div class=\"navbar-header\">\n" +
            "\n" +
            "            <a href=\"index.html\" class=\"navbar-brand\">SLiPP</a>\n" +
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
            "                        <button class=\"btn btn-default btn-primary\" type=\"submit\"><i\n" +
            "                                class=\"glyphicon glyphicon-search\"></i></button>\n" +
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
            "                <li><a href=\"./user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</nav>\n" +
            "<div class=\"navbar navbar-default\" id=\"subnav\">\n" +
            "    <div class=\"col-md-12\">\n" +
            "        <div class=\"navbar-header\">\n" +
            "            <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\"\n" +
            "               data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i\n" +
            "                    class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n" +
            "            <ul class=\"nav dropdown-menu\">\n" +
            "                <li><a href=\"user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a>\n" +
            "                </li>\n" +
            "                <li class=\"nav-divider\"></li>\n" +
            "                <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n" +
            "            </ul>\n" +
            "\n" +
            "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n" +
            "                <span class=\"sr-only\">Toggle navigation</span>\n" +
            "                <span class=\"icon-bar\"></span>\n" +
            "                <span class=\"icon-bar\"></span>\n" +
            "                <span class=\"icon-bar\"></span>\n" +
            "            </button>\n" +
            "        </div>\n" +
            "        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n" +
            "            <ul class=\"nav navbar-nav navbar-right\">\n" +
            "                <li class=\"active\"><a href=\"index.html\">Posts</a></li>\n" +
            "                <li><a href=\"user/login.html\" role=\"button\">로그인</a></li>\n" +
            "                <li><a href=\"user/form.html\" role=\"button\">회원가입</a></li>\n" +
            "                <!--\n" +
            "                <li><a href=\"#loginModal\" role=\"button\" data-toggle=\"modal\">로그인</a></li>\n" +
            "                <li><a href=\"#registerModal\" role=\"button\" data-toggle=\"modal\">회원가입</a></li>\n" +
            "                -->\n" +
            "                <li><a href=\"#\" role=\"button\">로그아웃</a></li>\n" +
            "                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n" +
            "\n" +
            "<div>\n" +
            "    <p>\n" +
            "        <h2>ERROR OCCURED!</h2>\n" +
            "    </p>\n" +
            "</div>\n" +
            "\n" +
            "<!-- script references -->\n" +
            "<script src=\"js/jquery-2.2.0.min.js\"></script>\n" +
            "<script src=\"js/bootstrap.min.js\"></script>\n" +
            "<script src=\"js/scripts.js\"></script>\n" +
            "</body>\n" +
            "</html>\n";

    private ExceptionHandler() {
    }

    public static void handleException(Exception exception, DataOutputStream dos) {
        // TODO Error Case에 따른 분류
        if (exception instanceof IOException) {

        }

        if (exception instanceof NullPointerException) {

        }
        // Other Exceptions
        // ...

        /**
         * TODO Error Page 출력
         * 그런데 그 와중에 나오는 error는 어떻게 처리해야 하나 ?
         * 1) Body를 읽어오는 도중 생기는 IO Exception방지를 위해 Body전체를 위와같이 저장 ..
         * 2) 읽는 오류는 해결(아마)되었는데, 출력 오류는 어떻게 해결 ?
         */
        try {
            ResponseHeader responseHeader = ResponseHeader.builder()
                    .uri(Links.ERROR)
                    .body(ERROR_PAGE_BODY.getBytes(StandardCharsets.UTF_8))
                    .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                    .accept("text/html")
                    .build();

            responseHeader.getHtmlResponseHeader()
                    .response(dos, responseHeader);
            responseHeader.responseBody(dos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
