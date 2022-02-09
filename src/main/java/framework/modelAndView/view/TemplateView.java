package framework.modelAndView.view;

import framework.modelAndView.ModelAndView;
import framework.modelAndView.View;
import framework.template.TemplateParser3;
import util.HttpRequest;
import util.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemplateView extends View {

    @Override
    public void render(ModelAndView mv, HttpRequest req, HttpResponse res) throws IOException {
        String templateName = mv.getViewName();
//        String template = new String(Files.readAllBytes(new File("./webapp/template" + templateName + ".html").toPath()));
        String template = Files.readString(Path.of("./webapp/template" + templateName + ".html"));

        TemplateParser3 templateParser3 = new TemplateParser3(template, mv.getModel());
        String renderedHtml = templateParser3.getTemplateWithModel();

        res.setBody(renderedHtml.getBytes(StandardCharsets.UTF_8));
        prepareResponse(req, res);
    }


    private void prepareResponse(HttpRequest req, HttpResponse res) {
        res.addHeader("Content-Type", "text/html");
        return ;
    }
}
