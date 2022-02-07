package myspring.reply;

import myspring.configures.web.ReplyRequestResolver;
import webserver.annotations.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reply")
public class ReplyRestController {

    private final ReplyService replyService;

    @Autowired
    public ReplyRestController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping(path = "/create")
    public ReplyDto createReply(@ReplyRequestResolver ReplyRequest replyRequest) {
        Reply reply = ReplyFactory.createReply(
                replyRequest.getUserSeq(),
                replyRequest.getArticleSeq(),
                replyRequest.getWriter(),
                replyRequest.getContents());
        replyService.createReply(reply);
        return new ReplyDto(reply);
    }

    @DeleteMapping(path = "/delete/{seq}")
    public Map<String, Boolean> deleteArticle(@PathVariable long seq, @ReplyRequestResolver ReplyRequest replyRequest) {
        Reply reply = ReplyFactory.createReply(
                seq,
                replyRequest.getUserSeq(),
                replyRequest.getArticleSeq());
        replyService.deleteReply(reply);
        return Map.of("valid", true);
    }

}
