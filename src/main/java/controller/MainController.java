package controller;

import annotation.RequestMapping;
import service.MemoService;
import webserver.html.HtmlBuilder;
import webserver.html.Model;
import webserver.http.HttpMethod;
import webserver.http.HttpStatus;
import webserver.response.Response;
import webserver.response.ResponseBuilder;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainController extends Controller {

    private static final MainController mainController = new MainController();
    private final MemoService memoService = MemoService.getInstance();

    private MainController() {
    }

    public static MainController getInstance() {
        return mainController;
    }

    @RequestMapping(method = HttpMethod.GET)
    private Response main(@Nullable Map<String, String> parameters) throws IOException {
        File file = new File("./webapp/index.html");
        Model model = new Model();
        model.setAttribute("memos", memoService.findAllOrderByDate());
        String html = new HtmlBuilder().build("./webapp/index.html", model);
        byte[] body = html.getBytes();
        return new ResponseBuilder().setHttpStatus(HttpStatus.OK)
                .setContent("text/html", body)
                .setBody(body)
                .build();
    }

}
