package webserver.view;

import org.apache.tika.Tika;
import webserver.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    private static final TemplateEngine templateEngine = new TemplateEngine();
    private static final Tika tika = new Tika();

    public void resolve(Response response, ModelAndView mv) throws IOException, IllegalAccessException {
        if(mv.getViewName().contains("redirect:")){
            response.writeRedirectHeader(mv.getViewName().split("redirect:")[1]);
            return;
        }
        File requestFile = new File("./webapp" + mv.getViewName());
        String mimeType = tika.detect(requestFile);
        byte[] body = templateEngine.render(mv, Files.readAllBytes(requestFile.toPath()));
        response.writeHeader(body.length, 200, mimeType);
        response.writeBody(body);
    }
}
