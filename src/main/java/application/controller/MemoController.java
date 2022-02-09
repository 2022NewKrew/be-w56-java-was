package application.controller;

import application.domain.MemoService;
import framework.modelAndView.ModelAndView;
import framework.util.RequestMapping;
import util.HttpRequest;
import util.HttpResponse;

public class MemoController implements Controller{

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @RequestMapping(path = "/memos", method = "GET")
    public String memos(ModelAndView mv, HttpRequest req, HttpResponse res) {
        mv.getModel().addAttribute("memos", memoService.getAllMemos());
        return "/memo/list";
    }
}
