package template;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TemplateEngine {

    public String render(String template, Map<String, Object> values) {
        return render(template, null, values);
    }

    private String render(String template, Object context, Map<String, Object> values) {
        String intermediate = renderList(template, values);
        return renderItem(intermediate, context, values);
    }

    private String renderList(String template, Map<String, Object> values) {
        Pattern pattern = Pattern.compile("\\{\\{#(.+?)}}");
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            return template;
        }
        String name = matcher.group(1);
        return renderList(name, template, values);
    }

    private String renderList(String name, String template, Map<String, Object> values) {
        String regex = String.format("\\{\\{#%s}}(.+?)\\{\\{/%s}}", name, name);
        int flags = Pattern.DOTALL | Pattern.MULTILINE;
        Pattern pattern = Pattern.compile(regex, flags);
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            return template;
        }
        String section = matcher.group(1);
        List<Object> list = getList(name, values);
        String rendered = list.stream()
                .map(item -> render(section, item, values))
                .collect(Collectors.joining());
        return matcher.replaceAll(rendered);
    }

    private String renderItem(String template, Object context, Map<String, Object> values) {
        Pattern pattern = Pattern.compile("\\{\\{([^#]+?)}}");
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            return template;
        }
        String name = matcher.group(1);
        return renderItem(name, template, context, values);
    }

    private String renderItem(String name, String template, Object context, Map<String, Object> values) {
        String regex = String.format("\\{\\{%s}}", name);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            return template;
        }
        String value = getItem(name, context, values).toString();
        return render(matcher.replaceAll(value), context, values);
    }

    private List<Object> getList(String name, Map<String, Object> values) {
        if (!values.containsKey(name)) {
            throw new MissingFormatArgumentException(name);
        }
        final Object rawList = values.get(name);
        if (!(rawList instanceof List)) {
            throw new ClassCastException();
        }
        //noinspection unchecked
        return (List<Object>) rawList;
    }

    private Object getItem(String name, Object context, Map<String, Object> values) {
        boolean valueInMap = values.containsKey(name);
        boolean valueInContext = hasField(context, name);
        if (!valueInMap && !valueInContext) {
            throw new MissingFormatArgumentException(name);
        }
        if (valueInContext) {
            return Optional.ofNullable(getField(context, name))
                    .map(Object::toString)
                    .orElse(null);
        }
        return values.get(name);
    }

    private Object getField(Object object, String name) {
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

    private boolean hasField(Object object, String name) {
        if (object == null) {
            return false;
        }
        try {
            object.getClass().getDeclaredField(name);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }
}
