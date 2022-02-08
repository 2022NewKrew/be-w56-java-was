package view;

import java.util.HashMap;
import java.util.Map;
import view.template.ArticleListTemplate;
import view.template.ListTemplate;
import view.template.ViewTemplate;

public enum ViewMatcher {
    LIST_VIEW("/user/list.html", ListTemplate.getInstance()),
    ARTICLE_LIST_VIEW("/index.html", ArticleListTemplate.getInstance());

    private static final Map<String, ViewTemplate> viewMap;

    static {
        viewMap = new HashMap<>();
        for (ViewMatcher viewMatcher : ViewMatcher.values()) {
            viewMap.put(viewMatcher.viewPath, viewMatcher.template);
        }
    }

    public static ViewTemplate getTemplate(String viewPath) {
        return viewMap.get(viewPath);
    }

    private final String viewPath;
    private final ViewTemplate template;

    ViewMatcher(String viewPath, ViewTemplate template) {
        this.viewPath = viewPath;
        this.template = template;
    }
}
