package bin.jayden.controller;

import bin.jayden.annotation.Controller;
import bin.jayden.annotation.GetMapping;
import bin.jayden.annotation.PostMapping;
import bin.jayden.http.MyHttpSession;
import bin.jayden.model.Article;
import bin.jayden.model.User;
import bin.jayden.service.ArticleService;

import java.io.IOException;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String articleList() throws IOException {
        return articleService.getArticleListHtml();
    }

    @PostMapping("/article/create")
    public String createArticle(String title, MyHttpSession session) {
        User user = (User) session.getAttribute("sessionUser");
        if (user == null)
            return "redirect:/user/login.html";
        articleService.createArticle(new Article(title, user.getId()));
        return "redirect:/";
    }
}
