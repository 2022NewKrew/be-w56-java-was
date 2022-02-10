package springmvc.controller;

import model.Memo;
import springmvc.ModelAndView;
import springmvc.db.MemoDataBase;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoController extends Controller {

    @Override
    protected ModelAndView doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            if (httpRequest.getCookies().getOrDefault("logined", "false").equals("true")) {
                List<Memo> memos = MemoDataBase.findAll();
                Map<String, Object> model = new HashMap<>();
                model.put("memos", memos);
                return new ModelAndView("/qna/list.html", HttpStatus.OK, model);
            }
            return new ModelAndView("/user/login.html", HttpStatus.FOUND);
        } catch (Exception e) {
            return new ModelAndView("/user/login.html", HttpStatus.BAD_REQUEST);
        }
    }
}
