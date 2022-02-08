package webserver.view;

import webserver.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    private static final TemplateEngine templateEngine = new TemplateEngine();

    public void resolve(Response response, ModelAndView mv) throws IOException, IllegalAccessException {
        if(mv.getViewName().contains("redirect:")){
            response.writeRedirectHeader(mv.getViewName().split("redirect:")[1]);
            return;
        }
        byte[] body = templateEngine.render(mv, Files.readAllBytes(new File("./webapp" + mv.getViewName()).toPath()));
        response.writeHeader(body.length, 200);
        response.writeBody(body);
    }
}
