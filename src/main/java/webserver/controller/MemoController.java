package webserver.controller;

import dto.MemoCreateDto;
import org.apache.commons.lang3.StringUtils;
import service.MemoService;
import util.annotation.RequestMapping;
import webserver.Request;
import webserver.Response;
import webserver.view.ModelAndView;

public class MemoController {
    private static final MemoController instance = new MemoController();
    private static final MemoService memoService = MemoService.getInstance();

    private MemoController() {}

    public static MemoController getInstance() {
        return instance;
    }

    @RequestMapping(value="/memos", method="POST")
    public ModelAndView create(Request request, Response response) {
        if(StringUtils.equals(request.getCookie("logined"), "true")){
            memoService.create(MemoCreateDto.builder()
                    .userId(Integer.parseInt(request.getCookie("userId")))
                    .content(request.getParameter("content"))
                    .build());
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/index.html");
            return mv;
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/user/login.html");
        return mv;
    }

    @RequestMapping(value="/index.html", method="GET")
    public ModelAndView findAll(Request request, Response response) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index.html");
        mv.addAttribute("memos", memoService.findAll());
        return mv;
    }
}
