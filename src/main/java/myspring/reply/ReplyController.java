package myspring.reply;

import myspring.configures.web.ReplyRequestResolver;
import webserver.annotations.*;

@Controller
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping(path = "/create")
    public String createReply(@ReplyRequestResolver ReplyRequest replyRequest) {
        replyService.createReply(ReplyFactory.createReply(
                replyRequest.getUserSeq(),
                replyRequest.getArticleSeq(),
                replyRequest.getWriter(),
                replyRequest.getContents()));
        return "redirect:/qna/show/" + replyRequest.getArticleSeq();
    }

    @DeleteMapping(path = "/delete/{seq}")
    public String deleteArticle(@PathVariable long seq, @ReplyRequestResolver ReplyRequest replyRequest) {
        replyService.deleteReply(ReplyFactory.createReply(
                seq,
                replyRequest.getUserSeq(),
                replyRequest.getArticleSeq()));
        return "redirect:/qna/show/" + replyRequest.getArticleSeq();
    }

}
