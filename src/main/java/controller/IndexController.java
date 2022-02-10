package controller;

import http.HttpMethod;
import model.ModelAndView;
import http.request.Request;
import service.ArticleService;
import service.ArticleServiceImpl;
import util.RequestMapping;

public class IndexController implements Controller{

    private static final IndexController indexController = new IndexController();
    private final ArticleService articleService = ArticleServiceImpl.getInstance();

    private IndexController(){}

    public static IndexController getInstance(){
        return indexController;
    }

    @RequestMapping(method = HttpMethod.GET, url = "/")
    public ModelAndView proceed(Request request) {
        return new ModelAndView("/", "articles", articleService.searchAllArticles());
    }
}
