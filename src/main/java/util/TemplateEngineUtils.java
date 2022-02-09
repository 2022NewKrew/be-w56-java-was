package util;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class TemplateEngineUtils {
    public static String renderDynamicTemplate(Object model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
