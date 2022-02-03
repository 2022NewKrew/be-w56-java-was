package controller;

import annotation.Controller;
import annotation.RequestMapping;
import model.Request;
import model.Response;
import service.ArticleService;
import service.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

@Controller("/article")
public class ArticleController {

    @RequestMapping(value = "/article/form", requestMethod = "GET")
    public static Response formRouting(Request request) throws IOException {
        if(UserService.isLoginState(request)) {
            return Response.of(request, "/qna/form.html", Files.readAllBytes(new File("./webapp" + "/qna/form.html").toPath()));
        }
        return Response.of(request, "/user/login.html", Files.readAllBytes(new File("./webapp" + "/user/login.html").toPath()));
    }

    @RequestMapping(value = "/article", requestMethod = "POST")
    public static Response saveRouting(Request request) throws IOException, SQLException {
        ArticleService.save(request);
        return Response.of(request, "/article/list",Files.readAllBytes(new File("./webapp" + "/index.html").toPath()));
    }

    @RequestMapping(value = "/article/list", requestMethod = "GET")
    public static Response articleListRouting(Request request) throws SQLException, IOException {
        byte[] body = ArticleService.articleListToByte();
        return Response.of(request, "/article/list", body);
    }
}
