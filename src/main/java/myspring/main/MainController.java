package myspring.main;

import myspring.article.ArticleDto;
import myspring.article.ArticleService;
import webserver.annotations.Autowired;
import webserver.annotations.Controller;
import webserver.annotations.GetMapping;
import webserver.context.Model;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    private final ArticleService articleService;

    @Autowired
    public MainController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(path = {"/", "/index"})
    public String createUserForm(SimplePageRequest simplePageRequest, Model model) {
        List<ArticleDto> articles = articleService.getArticlesByRange(simplePageRequest.getStartPageIndex(), simplePageRequest.getEndPageIndex()).stream().map(ArticleDto::new).collect(Collectors.toList());
        model.addAttribute("articles", articles);
        PageBarDto pages = PageBarDto.of(simplePageRequest.getStartBarIndex(), simplePageRequest.getEndBarIndex(), simplePageRequest.getTotalPageCount(articleService.getAllArticlesCount()), simplePageRequest.getSize());
        model.addAttribute("pages", pages);
        return "index";
    }

}
