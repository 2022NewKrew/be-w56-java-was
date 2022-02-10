package controller;

import db.DataBase;
import model.Post;
import webserver.annotation.Controller;
import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;
import webserver.annotation.RequestParam;
import webserver.domain.Cookie;
import webserver.domain.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PostController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("posts", DataBase.findAllPosts());
        return "/index";
    }

    @GetMapping("/post")
    public String postForm(Cookie cookie) {
        String logined = cookie.getData("logined");
        if(logined == null || logined.equals("false")) {
            return "redirect:/user/login.html";
        }
        return "/qna/form";
    }

    @PostMapping("/post")
    public String post(@RequestParam("author")String author,
                       @RequestParam("content")String content) {
        DataBase.addPost(new Post(author, content, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))));
        return "redirect:/";
    }
}
