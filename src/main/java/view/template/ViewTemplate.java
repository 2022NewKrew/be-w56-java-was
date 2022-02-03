package view.template;

import java.util.Map;

public interface ViewTemplate {
    String getTemplate(Map<String, Object> model);
}
