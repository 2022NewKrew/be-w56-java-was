package http.util;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlBuilder {
    private static final String startRegex = "\\{\\{#.+}}";
    private static final String contentRegex = "\\{\\{\\w+}}";
    private static final String endRegex = "\\{\\{/.+}}";
    private static final String indexRegex = "\\{\\{@index}}";

    private static final Pattern startPattern = Pattern.compile(startRegex);
    private static final Pattern contentPattern = Pattern.compile(contentRegex);
    private static final Pattern endPattern = Pattern.compile(endRegex);
    private static final Pattern indexPattern = Pattern.compile(indexRegex);

    public static String build(Model model, File file) throws IOException {
        if(Objects.isNull(model) || Objects.isNull(file)){
            throw new IllegalArgumentException();
        }

        StringBuilder html = new StringBuilder();
        String fileString = Files.readString(file.toPath());

        Matcher startMatcher = startPattern.matcher(fileString);
        Matcher endMatcher = endPattern.matcher(fileString);


        if(startMatcher.find() && endMatcher.find()){
            String htmlHeader = fileString.substring(0, startMatcher.start());
            String htmlContent = fileString.substring(startMatcher.end(), endMatcher.start());
            String htmlFooter = fileString.substring(endMatcher.end());

            html.append(htmlHeader);
            html.append(buildContents(htmlContent, model, startMatcher.group()));
            html.append(htmlFooter);
        }

        return html.toString();
    }

    private static StringBuilder buildContents(String html, Model model, String pattern){
        StringBuilder contents = new StringBuilder();
        String key = pattern.substring(3, pattern.length() - 2);
        Collection<Object> objects = (Collection<Object>) model.getAttribute(key);
        List<String> fields = getAllFields(html);
        AtomicReference<Integer> index = new AtomicReference<>(1);

        objects.forEach(obj ->{
            contents.append(buildContent(html, obj, fields, index.getAndSet(index.get() + 1)));
        });

        return contents;
    }

    private static String buildContent(String html, Object object, List<String> fields, Integer index){
        for (String field : fields) {
            try {
                String fieldName = field.substring(2, field.length() - 2);
                Method method = object.getClass().getMethod(
                        "get" +
                                fieldName.substring(0, 1).toUpperCase() +
                                fieldName.substring(1)
                );
                html = html.replace(field, method.invoke(object).toString());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Matcher indexMatcher = indexPattern.matcher(html);
        if(indexMatcher.find()){
            html = html.replaceAll(indexRegex, index.toString());
        }
        System.out.println(html);
        return html;
    }

    private static List<String> getAllFields(String html){
        Matcher contentMatcher = contentPattern.matcher(html);
        List<String> fields = new ArrayList<>();

        while(contentMatcher.find()){
            fields.add(contentMatcher.group());
        }

        return fields;
    }
}
