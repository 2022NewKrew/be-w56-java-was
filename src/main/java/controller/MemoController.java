package controller;

import dto.memo.MemoCreateDto;
import exception.BusinessException;
import service.MemberService;
import service.MemoService;
import webserver.annotations.Autowired;
import webserver.annotations.Component;
import webserver.annotations.GetMapping;
import webserver.annotations.PostMapping;
import webserver.model.ModelAndView;
import webserver.model.http.HttpRequest;

@Component
public class MemoController {
    private final MemoService memoService;
    private final MemberService memberService;

    @Autowired
    public MemoController(MemoService memoService,
                          MemberService memberService) {
        this.memoService = memoService;
        this.memberService = memberService;
    }

    @GetMapping("/qna/form.html")
    public ModelAndView getQnaForm(HttpRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        if (request.getCookie("sessionedUser") == null)
            modelAndView.setViewName("redirect:/user/login.html");
        else
            modelAndView.setViewName("/qna/form.html");
        return modelAndView;
    }

    @PostMapping("/qna/create")
    public ModelAndView createMemo(MemoCreateDto memoCreateDto, HttpRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        String sessionedUserId = request.getCookie("sessionedUser");
        if (sessionedUserId == null) {
            modelAndView.setViewName("redirect:/user/login.html");
            return modelAndView;
        }

        try {
            memoService.create(memoCreateDto, sessionedUserId);
            modelAndView.setViewName("redirect:/index.html");
        } catch (BusinessException e) {
            modelAndView.setViewName("redirect:/index.html");
        }
        return modelAndView;
    }
}
