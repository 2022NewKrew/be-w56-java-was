package webserver.view;

import lombok.extern.slf4j.Slf4j;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class ModelAndView {
    private static final String VIEW_DIR = "./src/main/resources/static";
    private final Map<String, Object> models;
    private final String viewName;
    
    /**
     * Basic view without model
     *
     * @param viewName
     */
    public ModelAndView(String viewName) {
        this.viewName = viewName;
        models = null;
    }

    public void render(HttpRequest httpRequest, HttpResponse httpResponse) {
        Path path = Paths.get(resolve(viewName));

        if (models == null) {
            try {
                httpResponse.setHttpStatus(HttpStatus.OK);
                httpResponse.setContentTypeWithURI(path.toAbsolutePath().toString());
                httpResponse.setBody(Files.readAllBytes(path));
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }

        // Templating when models not null
    }

    public String resolve(String viewName) {
        // Some strategy to resolve view
        return VIEW_DIR + viewName;
    }
}
