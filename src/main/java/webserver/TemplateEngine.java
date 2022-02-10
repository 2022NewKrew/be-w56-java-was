package webserver;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TemplateEngine {

    private static final String SINGLE_REGEX = "\\{\\{([^#].+?)}}";
    private static final String LOOP_REGEX = "\\{\\{#(.+?)}}(.+?)\\{\\{/(.+?)}}";

    public static String render(String template, Model model) {
        return render(template, null, model);
    }

    private static String render(String template, Object item, Model model) {
        String renderLoop = renderLoop(template, model);
        return renderSingle(renderLoop, item);
    }

    private static String renderLoop(String template, Model model) {
        Pattern pattern = Pattern.compile(LOOP_REGEX, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            return template;
        }
        String name = matcher.group(1);
        List<Object> list = (List<Object>) model.getAttribute(name);
        String rendered = list.stream()
            .map(item -> render(matcher.group(2), item, model))
            .collect(Collectors.joining());
        return matcher.replaceAll(rendered);
    }

    private static String renderSingle(String template, Object item) {
        Pattern pattern = Pattern.compile(SINGLE_REGEX);
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            return template;
        }
        String name = matcher.group(1);
        String value = getField(name, item).toString();
        return renderSingle(matcher.replaceFirst(value), item);
    }

    private static Object getField(String name, Object object) {
        if (object == null) {
            return null;
        }
        try {
            Field field = object.getClass().getDeclaredField(name);
            boolean accessible = field.canAccess(object);
            field.setAccessible(true);
            Object value = field.get(object);
            field.setAccessible(accessible);
            return value;
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
            return null;
        }
    }
}
