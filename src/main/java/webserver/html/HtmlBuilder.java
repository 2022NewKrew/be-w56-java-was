package webserver.html;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlBuilder {

    private static final String startRegex = "\\{\\{#.+}}";
    private static final String endRegex = "\\{\\{/.+}}";
    private static final String contentRegex = "\\{\\{\\w+}}";
    private static final String indexRegex = "\\{\\{@index}}";
    private static final Pattern startPattern = Pattern.compile(startRegex);
    private static final Pattern endPattern = Pattern.compile(endRegex);
    private static final Pattern contentPattern = Pattern.compile(contentRegex);
    private static final Pattern indexPattern = Pattern.compile(indexRegex);

    public <T> String build(String url, Model model) throws IOException {
        String file = Files.readString(new File(url).toPath());
        StringBuilder html = new StringBuilder();
        Matcher startMatcher = startPattern.matcher(file);
        Matcher endMatcher = endPattern.matcher(file);
        if (startMatcher.find() && endMatcher.find()) {
            String htmlHeader = file.substring(0, startMatcher.start());
            String htmlFooter = file.substring(endMatcher.end());
            String content = file.substring(startMatcher.end(), endMatcher.start());
            StringBuilder contents = makeContents(content, model, startMatcher.group());
            html.append(htmlHeader);
            html.append(contents);
            html.append(htmlFooter);
        }
        return html.toString();
    }

    private StringBuilder makeContents(String content, Model model, String matchedPattern) {
        StringBuilder contents = new StringBuilder();
        String key = matchedPattern.substring(3, matchedPattern.length() - 2);
        List<String> fieldList = findAllField(content);
        Collection<Object> attribute = (Collection<Object>) model.getAttribute(key);
        AtomicInteger count = new AtomicInteger(1);
        attribute.forEach(obj -> {
            try {
                contents.append(makeContent(content, fieldList, obj.getClass(), obj, count.get()));
                count.getAndIncrement();
            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        return contents;
    }

    private <T> String makeContent(String content, List<String> fieldList, Class<T> classType, Object obj, int count) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (String matchedField : fieldList) {
            String fieldName = matchedField.substring(2, matchedField.length() - 2);
            Method method = classType.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            content = content.replace(matchedField, method.invoke(obj).toString());
        }
        Matcher indexMatcher = indexPattern.matcher(content);
        if (indexMatcher.find()) {
            content = content.replaceAll(indexRegex, String.valueOf(count));
        }
        return content;
    }

    private List<String> findAllField(String content) {
        Matcher contentMatcher = contentPattern.matcher(content);
        List<String> fieldList = new ArrayList<>();
        while (contentMatcher.find()) {
            String matchedField = contentMatcher.group();
            fieldList.add(matchedField);
        }
        return fieldList;
    }

}
