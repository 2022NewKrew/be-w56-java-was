package framework.modelAndView.view;

import framework.modelAndView.ModelAndView;
import framework.modelAndView.View;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticView extends View {

    @Override
    public void render(ModelAndView mv, HttpRequest req, HttpResponse res) throws IOException {
        String viewName = mv.getViewName();

        byte[] body = Files.readAllBytes(new File("./webapp" + viewName).toPath());
        res.setBody(body);

        prepareResponse(req, res);
        }


    private void prepareResponse(HttpRequest req, HttpResponse res) {
        res.addHeader("Content-Type", "text/html");
        return ;
    }

}
