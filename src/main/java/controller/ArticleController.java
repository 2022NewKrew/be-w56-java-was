package controller;

import http.HttpMethod;
import http.request.Request;
import model.Article;
import model.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArticleService;
import service.ArticleServiceImpl;
import util.RequestMapping;

public class ArticleController implements Controller{
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private static final ArticleController articleController = new ArticleController();
    private final ArticleService articleService = ArticleServiceImpl.getInstance();

    private ArticleController(){}

    public static ArticleController getInstance(){
        return articleController;
    }

    @RequestMapping(method = HttpMethod.GET, url = "/articles/form")
    public ModelAndView createArticleForm(Request request){
        log.info("[ArticleController] : createArticleForm");
        if(request.isLogin()){
            return new ModelAndView("/articles/form");
        }
        return new ModelAndView("redirect:/users/login");
    }

    @RequestMapping(method = HttpMethod.POST, url = "/articles")
    public ModelAndView writeArticle(Request request){
        log.info("[ArticleController] : writeArticle");
        Article article = articleService.createArticle(request.getRequestBody());
        return new ModelAndView("redirect:/", "article", article);
    }


}
