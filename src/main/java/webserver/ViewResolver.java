package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    public void resolve(Response response, String view) throws IOException {
        if(view.contains("redirect:")){
            response.writeRedirectHeader(view.split("redirect:")[1]);
            return;
        }
        byte[] body = Files.readAllBytes(new File("./webapp" + view).toPath());
        response.writeHeader(body.length, 200);
        response.writeBody(body);
    }
}
