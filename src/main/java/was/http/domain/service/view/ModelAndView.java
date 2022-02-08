package was.http.domain.service.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelAndView {
    private final ViewType viewType;
    private final String path;
    private final Map<String, Object> models = new HashMap<>();

    public ModelAndView(ViewType viewType, String path) {
        this.viewType = viewType;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public Optional<Object> getModel(String key) {
        return Optional.ofNullable(models.get(key));
    }

    public Map<String, Object> getModels() {
        return models;
    }

    public ModelAndView addModel(String key, Object value) {
        models.put(key, value);
        return this;
    }
}
