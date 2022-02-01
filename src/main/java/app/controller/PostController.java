package app.controller;

import app.model.Post;
import app.model.Posts;
import app.repository.PostsRepository;
import configure.DbConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.annotation.components.Controller;
import util.annotation.mapping.GetMapping;
import util.annotation.mapping.PostMapping;
import util.ui.Model;

import java.sql.SQLException;

@Controller
public class PostController {
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    private final PostsRepository postsRepository;
    // Todo DI 구현
    public PostController(PostsRepository postsRepository){
        this.postsRepository = postsRepository;
    }

    public PostController() throws SQLException {
        this.postsRepository = new PostsRepository(new DbConfigure().getConnection());
    }

    @GetMapping(url="/posts")
    public String posts(Model model) throws SQLException {
        Posts posts = postsRepository.findAll();
        model.addAttribute("posts", posts.getPosts());
        return "/post/posts.html";
    }

    @PostMapping(url="/post")
    public String insertPost(Post post) throws SQLException {
        postsRepository.insert(post);
        return "redirect:/posts";
    }

}
