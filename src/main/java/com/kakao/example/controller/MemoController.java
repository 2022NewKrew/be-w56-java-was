package com.kakao.example.controller;

import com.kakao.example.application.dto.MemoDto;
import com.kakao.example.application.service.MemoService;
import com.kakao.example.util.exception.NotLoggedInException;
import framework.util.HttpSession;
import framework.util.annotation.Autowired;
import framework.util.annotation.Component;
import framework.util.annotation.RequestMapping;
import framework.util.annotation.ResponseBody;
import framework.view.ModelView;
import framework.webserver.HttpRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static framework.util.annotation.Component.ComponentType.CONTROLLER;

@Component(type = CONTROLLER)
@RequestMapping(value = "/memo")
public class MemoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoController.class);

    private final MemoService memoService;

    @Autowired
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @RequestMapping(value = "/add", requestMethod = "POST")
    @ResponseBody
    public void addMemo(HttpRequestHandler request, ModelView modelView) {
        HttpSession session = request.getSession();

        if (!session.contains("USER_ID")) {
            throw new NotLoggedInException();
        }

        String userId = (String) session.getAttribute("USER_ID");
        String content = request.getAttribute("content");

        MemoDto memo = memoService.addMemo(MemoDto.builder()
                .writerId(userId)
                .content(content)
                .build());

        modelView.setAttribute("memo", memo);
    }

    @RequestMapping(value = "/list", requestMethod = "GET")
    @ResponseBody
    public void memoList(ModelView modelView) {
        LOGGER.debug("Get all memos");
        modelView.setAttribute("memos", memoService.findAll());
    }
}
