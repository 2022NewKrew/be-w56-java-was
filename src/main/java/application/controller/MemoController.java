package application.controller;

import application.domain.Memo;
import application.domain.MemoService;
import framework.modelAndView.ModelAndView;
import framework.annotation.RequestMapping;
import webserver.request.HttpRequest;
import webserver.request.HttpRequestUtils;
import webserver.response.HttpResponse;

import java.time.LocalDate;
import java.util.Map;

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

    @RequestMapping(path = "/memos", method = "POST")
    public String registerMemos(ModelAndView mv, HttpRequest req, HttpResponse res) {
        if (!req.getCookie().getCookieByKey("logined").equals("true"))
            return "/user/login.html";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(req.getBody());
        Memo memo = new Memo(parameters.get("writer"), parameters.get("contents"), LocalDate.now());
        memoService.save(memo);
        return "redirect:/memos";
    }
}
