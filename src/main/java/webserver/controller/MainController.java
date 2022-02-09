package webserver.controller;

import domain.db.MemoRepository;
import domain.model.Memo;
import webserver.http.domain.HttpMethod;
import webserver.http.domain.MethodAndUrl;
import webserver.http.response.Model;

import java.util.List;

public class MainController extends Controller{

    public MainController() {
        runner.put(new MethodAndUrl(HttpMethod.GET, "/"), (req, res) -> {
            Model model = res.getModel();
            List<Memo> memos = MemoRepository.findAll();
            model.addAttribute("memos", memos);

            res.setUrl("/index.html")
                    .forward();
        });

        runner.put(new MethodAndUrl(HttpMethod.POST, "/memo"), (req, res) -> {
            Memo memo = Memo.of(req.getParams());
            MemoRepository.create(memo);

            res.setStatusCode(302, "Found")
                    .setUrl("/")
                    .forward();
        });
    }
}
