package webserver;

import exception.PageNotFoundException;
import exception.TemplateSyntaxException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyTemplateView implements TemplateView {

    private static final String START_TAG_REGEX = "\\{\\{#?[a-zA-Z]\\w*}}";
    private static final String LIST_START_TAG = "{{#";
    private static final String LIST_END_TAG = "{{/";
    private static MyTemplateView instance;

    private MyTemplateView() {
    }

    public static MyTemplateView getInstance() {
        if (instance == null) {
            instance = new MyTemplateView();
        }
        return instance;
    }

    @Override
    public byte[] renderTemplateModel(String viewName, Model model)
            throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String template = new String(loadStaticFile(viewName));
        String renderedTemplate = execute(new StringBuilder(template), model);
        return renderedTemplate.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] loadStaticFile(String viewName) throws IOException {
        Path path = new File(STATIC_FILE_PATH + viewName).toPath();
        if (Files.notExists(path)) {
            throw new PageNotFoundException();
        }
        return Files.readAllBytes(path);
    }

    private String execute(StringBuilder template, Object model) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pattern pattern = Pattern.compile(START_TAG_REGEX);
        Matcher matcher = pattern.matcher(template);
        if (!matcher.find()) {
            return template.toString();
        }

        String startTag = matcher.group();
        if (startTag.startsWith(LIST_START_TAG)) {
            replaceList(startTag, template, model);
            return execute(template, model);
        }

        replaceVariable(startTag, template, model);
        return execute(template, model);
    }

    private void replaceVariable(String startTag, StringBuilder template, Object model)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        int startIdx = template.indexOf(startTag);
        int endIdx = startIdx + startTag.length();

        String key = getKey(startTag);
        String value = (String) getValue(model, key);
        template.replace(startIdx, endIdx, value);
    }

    private void replaceList(String startTag, StringBuilder template, Object model)
            throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String endTag = startTag.replace(LIST_START_TAG, LIST_END_TAG);
        if (template.indexOf(endTag) < 0) {
            throw new TemplateSyntaxException("end tag 를 찾을 수 없습니다.");
        }

        int startIdx = template.indexOf(startTag);
        int endIdx = template.indexOf(endTag) + endTag.length();

        StringBuilder sb = new StringBuilder();
        String key = getKey(startTag);
        Iterable<?> objects = (Iterable<?>) getValue(model, key);
        for (Object object : objects) {
            sb.append(execute(new StringBuilder(template.substring(startIdx + startTag.length(), endIdx - endTag.length())), object));
        }

        template.replace(startIdx, endIdx, sb.toString());
    }

    private String getKey(String tag) {
        if (tag.startsWith(LIST_START_TAG)) {
            return tag.substring(3, tag.length() - 2);
        }
        return tag.substring(2, tag.length() - 2);
    }

    private Object getValue(Object model, String key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (model instanceof Model) {
            return ((Model) model).getValue(key);
        }

        Class<?> modelClass = model.getClass();
        final Method getter = getMethod(modelClass, key);
        return getter.invoke(model);
    }

    private Method getMethod(Class<?> modelClass, String key) throws NoSuchMethodException {
        String upperKey = Character.toUpperCase(key.charAt(0)) + key.substring(1);
        return modelClass.getDeclaredMethod("get" + upperKey);
    }
}
