package myspring.article;

import myspring.configures.web.ArticleRequestResolver;
import myspring.reply.Reply;
import myspring.reply.ReplyDto;
import myspring.reply.ReplyService;
import webserver.annotations.*;
import webserver.context.Model;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/qna")
public class ArticleController {

    private final ArticleService articleService;

    private final ReplyService replyService;

    @Autowired
    public ArticleController(ArticleService articleService, ReplyService replyService) {
        this.articleService = articleService;
        this.replyService = replyService;
    }

    @GetMapping(path = "/form")
    public String createArticleForm() {
        return "qna/form";
    }

    @GetMapping(path = "/show/{seq}")
    public String showArticle(@PathVariable long seq, @ArticleRequestResolver ArticleRequest articleRequest, Model model) {
        Article article = articleService.findBySeq(seq);
        model.addAttribute("article", new ArticleDto(article, articleRequest.getUserSeq()));
        List<Reply> replys = replyService.findByArticleSeq(article.getSeq());
        model.addAttribute("replysCount", replys.size());
        model.addAttribute("replys", replys.stream().map(reply -> new ReplyDto(reply, articleRequest.getUserSeq())).collect(Collectors.toList()));
        return "qna/show";
    }

    @PostMapping(path = "/create")
    public String createArticle(@ArticleRequestResolver ArticleRequest articleRequest) {
        articleService.createArticle(ArticleFactory.createArticle(
                articleRequest.getUserSeq(),
                articleRequest.getWriter(),
                articleRequest.getTitle(),
                articleRequest.getContents()));
        return "redirect:/";
    }

    @GetMapping(path = "/update/{seq}")
    public String updateArticlePage(@PathVariable long seq, Model model) {
        Article article = articleService.findBySeq(seq);
        model.addAttribute("article", new ArticleDto(article));
        return "qna/updateForm";
    }

    @PutMapping(path = "/update/{seq}")
    public String updateArticle(@PathVariable long seq, @ArticleRequestResolver ArticleRequest articleRequest) {
        articleService.updateArticle(ArticleFactory.createArticle(
                seq,
                articleRequest.getUserSeq(),
                articleRequest.getWriter(),
                articleRequest.getTitle(),
                articleRequest.getContents()));
        return "redirect:/qna/show/" + seq;
    }

    @DeleteMapping(path = "/delete/{seq}")
    public String deleteArticle(@PathVariable long seq, @ArticleRequestResolver ArticleRequest articleRequest) {
        articleService.deleteArticle(ArticleFactory.createArticle(
                seq,
                articleRequest.getUserSeq()));
        return "redirect:/";
    }
}
