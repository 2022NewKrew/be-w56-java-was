package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            log.debug("request line : {}", line);
            String path = HttpRequestUtils.getPath(line);
            if(line==null){
                return;
            }

            Map<String, String> headers = new HashMap<>();
            while(!line.equals("")){
                line = br.readLine();
                log.debug("request : {}", line);
                String[] headerTokens = line.split(": ");
                if(headerTokens.length == 2){
                    headers.put(headerTokens[0], headerTokens[1]);
                }
            }

            log.debug("Content-Length : {}", headers.get("Content-Length"));

            if(path.startsWith("/user/create")){
                String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                log.debug("Request Body : {}", HttpRequestUtils.parseQueryString(requestBody));
                Map<String,String> params = HttpRequestUtils.parseQueryString(requestBody);
                User user = new User(params.get("userId"),params.get("password"),params.get("name"),params.get("email"));
                log.debug("User : {}", user);
                DataBase.addUser(user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
            } else if(path.equals("/user/login")){
                String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                log.debug("Request Body : {}", HttpRequestUtils.parseQueryString(requestBody));
                Map<String,String> params = HttpRequestUtils.parseQueryString(requestBody);
                log.debug("userId : {}, password : {}", params.get("userId"), params.get("password"));
                User user = DataBase.getUser(params.get("userId"));
                if(user==null){
                    log.debug("User Not Found!");
                    DataOutputStream dos = new DataOutputStream(out);
                    response302Header(dos);
                } else if(user.getPassword().equals(params.get("password"))){
                    log.debug("login success!");
                    DataOutputStream dos = new DataOutputStream(out);
                    response302HeaderWithCookie(dos, "logined=true");
                } else{
                    log.debug(("Password Mismatch!"));
                    DataOutputStream dos = new DataOutputStream(out);
                    response302Header(dos);
                }
            } else if(path.startsWith("/user/list")){
                log.debug("user/list");
                StringBuilder sb = new StringBuilder();
                ArrayList<User> users = new ArrayList<>(DataBase.findAll());

                sb.append("<!DOCTYPE html>\n"+
                    "<html lang=\"kr\">\n"+
                    "<head>\n"+
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n"+
                        "<meta charset=\"utf-8\">\n"+
                        "<title>SLiPP Java Web Programming</title>\n"+
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n"+
                        "<link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">\n"+
                        "<!--[if lt IE 9]>\n"+
                        "<script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n"+
                        "<![endif]-->\n"+
                        "<link href=\"../css/styles.css\" rel=\"stylesheet\">\n"+
                    "</head>\n"+
                    "<body>\n"+
                    "<nav class=\"navbar navbar-fixed-top header\">\n"+
                        "<div class=\"col-md-12\">\n"+
                            "<div class=\"navbar-header\">\n"+
                                "<a href=\"../index.html\" class=\"navbar-brand\">SLiPP</a>\n"+
                                "<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n"+
                                    "<i class=\"glyphicon glyphicon-search\"></i>\n"+
                                "</button>\n"+
                            "</div>\n"+
                            "<div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n"+
                                "<form class=\"navbar-form pull-left\">\n"+
                                    "<div class=\"input-group\" style=\"max-width:470px;\">\n"+
                                        "<input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n"+
                                        "<div class=\"input-group-btn\">\n"+
                                            "<button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n"+
                                        "</div>\n"+
                                    "</div>\n"+
                                "</form>\n"+
                                "<ul class=\"nav navbar-nav navbar-right\">\n"+
                                    "<li>\n"+
                                        "<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n"+
                                        "<ul class=\"dropdown-menu\">\n"+
                                            "<li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n"+
                                            "<li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n"+
                                        "</ul>\n"+
                                    "</li>\n"+
                                    "<li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n"+
                                "</ul>\n"+
                            "</div>\n"+
                        "</div>\n"+
                    "</nav>\n"+
                    "<div class=\"navbar navbar-default\" id=\"subnav\">\n"+
                        "<div class=\"col-md-12\">\n"+
                            "<div class=\"navbar-header\">\n"+
                                "<a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n"+
                                "<ul class=\"nav dropdown-menu\">\n"+
                                    "<li><a href=\"../user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n"+
                                    "<li class=\"nav-divider\"></li>\n"+
                                    "<li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n"+
                                "</ul>\n"+
                                "<button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n"+
                                    "<span class=\"sr-only\">Toggle navigation</span>\n"+
                                    "<span class=\"icon-bar\"></span>\n"+
                                    "<span class=\"icon-bar\"></span>\n"+
                                    "<span class=\"icon-bar\"></span>\n"+
                                "</button>\n"+
                            "</div>\n"+
                            "<div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n"+
                                "<ul class=\"nav navbar-nav navbar-right\">\n"+
                                    "<li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n"+
                                    "<li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>\n"+
                                    "<li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>\n"+
                                    "<li><a href=\"#\" role=\"button\">로그아웃</a></li>\n"+
                                    "<li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n"+
                                "</ul>\n"+
                            "</div>\n"+
                        "</div>\n"+
                    "</div>\n"+
                    "<div class=\"container\" id=\"main\">\n"+
                       "<div class=\"col-md-10 col-md-offset-1\">\n"+
                          "<div class=\"panel panel-default\">\n"+
                              "<table class=\"table table-hover\">\n"+
                                  "<thead>\n"+
                                    "<tr>\n"+
                                        "<th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>\n"+
                                    "</tr>\n"+
                                  "</thead>\n"+
                                  "<tbody>\n");
                int idx = 1;
                for(User u:users){
                    sb.append(String.format("<tr>\n"+
                                "<th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n"+
                            "</tr>",idx++,u.getUserId(),u.getName(),u.getEmail()));
                }

                sb.append("</tbody>\n"+
                              "</table>\n"+
                            "</div>\n"+
                        "</div>\n"+
                    "</div>\n"+
                    "<!-- script references -->\n"+
                    "<script src=\"../js/jquery-2.2.0.min.js\"></script>\n"+
                    "<script src=\"../js/bootstrap.min.js\"></script>\n"+
                    "<script src=\"../js/scripts.js\"></script>\n"+
                        "</body>\n"+
                    "</html>");

                File file = new File("./webapp/user/list.html");
                try{
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    bw.write(sb.toString());
                    bw.close();
                    DataOutputStream dos = new DataOutputStream(out);
                    byte[] body = Files.readAllBytes(new File("./webapp/user/list.html").toPath());
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }catch(Exception e){
                    log.debug(e.getMessage());
                }
            }else if(path.endsWith(".css")){
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp"+path).toPath());
                response200HeaderWithCss(dos, body.length);
                responseBody(dos, body);
            }else{
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = Files.readAllBytes(new File("./webapp"+path).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200HeaderWithCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html\r\n");
            dos.writeBytes("Set-Cookie: "+cookie+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
