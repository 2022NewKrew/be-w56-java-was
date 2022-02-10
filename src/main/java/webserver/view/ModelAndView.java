package webserver.view;

import lombok.extern.slf4j.Slf4j;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ModelAndView {
    private static final String TAG_TEMPLATE_PREFIX = "{{";
    private static final String TAG_TEMPLATE_POSTFIX = "}}";
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
        models = new HashMap<>();
    }

    public void render(HttpResponse httpResponse) {
        Path path = Paths.get(resolve(viewName));
        httpResponse.setHttpStatus(HttpStatus.OK);
        httpResponse.setContentTypeWithURI(path.toAbsolutePath().toString());

        if (models.isEmpty()) {
            renderStatic(path, httpResponse);
        } else {
            renderWithTemplate(path, models, httpResponse);
        }
    }

    private void renderStatic(Path path, HttpResponse httpResponse) {
        try {
            httpResponse.setBody(Files.readAllBytes(path));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void renderWithTemplate(Path path, Map<String, Object> models, HttpResponse httpResponse) {
        try {
            String result = Files.readString(path);
            for (var model : models.entrySet()) {
                result = result.replace(getTemplatedTag(model.getKey()), model.getValue().toString());
            }
            httpResponse.setBody(result);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getTemplatedTag(String tagName) {
        return TAG_TEMPLATE_PREFIX + tagName + TAG_TEMPLATE_POSTFIX;
    }

    private String resolve(String viewName) {
        // Some strategy to resolve view
        return VIEW_DIR + viewName;
    }

    public void addAttribute(String name, Object value) {
        models.put(name, value);
    }
}
