package springmvc.controller;

import model.Memo;
import model.User;
import springmvc.ModelAndView;
import springmvc.db.DataBase;
import springmvc.db.MemoDataBase;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

public class MemoCreateController extends Controller {

    @Override
    protected ModelAndView doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.getCookies().getOrDefault("logined", "false").equals("true")){
            return new ModelAndView("/qna/form.html", HttpStatus.OK);
        }
        return new ModelAndView("/user/login.html", HttpStatus.FOUND);
    }

    @Override
    protected ModelAndView doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            if (httpRequest.getCookies().getOrDefault("logined", "false").equals("true")){
                Map<String, String> body = httpRequest.getBody();
                Memo memo = new Memo(body.get("name"), body.get("content"), LocalDateTime.now());
                MemoDataBase.save(memo);
                return new ModelAndView("/", HttpStatus.FOUND);
            }
            return new ModelAndView("/user/login.html", HttpStatus.FOUND);
        } catch (Exception e) {
            return new ModelAndView("/user/login.html", HttpStatus.BAD_REQUEST);
        }
    }
}
