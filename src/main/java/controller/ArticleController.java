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
    private static final String ROOT_PATH = "./webapp";
    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController() {
        this.articleService = new ArticleService();
        this.userService = new UserService();
    }

    @RequestMapping(value = "/article/form", requestMethod = "GET")
    public Response formRouting(Request request) throws IOException {
        if (userService.isLoginState(request)) {
            return Response.of(request, "/qna/form.html", Files.readAllBytes(new File(ROOT_PATH + "/qna/form.html").toPath()));
        }
        return Response.of(request, "/user/login.html", Files.readAllBytes(new File(ROOT_PATH + "/user/login.html").toPath()));
    }

    @RequestMapping(value = "/article", requestMethod = "POST")
    public Response saveRouting(Request request) throws IOException, SQLException {
        articleService.save(request);
        return Response.of(request, "/article/list", Files.readAllBytes(new File(ROOT_PATH + "/index.html").toPath()));
    }

    @RequestMapping(value = "/article/list", requestMethod = "GET")
    public Response articleListRouting(Request request) throws SQLException, IOException {
        byte[] body = articleService.articleListToByte();
        return Response.of(request, "/article/list", body);
    }
}
