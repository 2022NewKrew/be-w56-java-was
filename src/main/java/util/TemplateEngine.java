package util;

import util.ui.Model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {

    private static final String startRegex = "\\{\\{#[a-zA-Z]+\\}\\}";
    private static final String endRegex = "\\{\\{/[a-zA-Z]+\\}\\}";
    private static final String valueRegex = "\\{\\{[a-zA-Z]+\\}\\}";

    public static byte[] render(String template, Model model) throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String templateString = Files.readString(Path.of("./webapp" + template));
        return divideByList(templateString, model).toString().getBytes(StandardCharsets.UTF_8);
    }

    private static Object getValue(Object object, String key) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if(Model.class.isAssignableFrom(object.getClass()))
            return ((Model) object).getAttribute(key);
        // if object is not an implementation of model.
        StringBuilder sb = new StringBuilder(key);
        sb.replace(0,1, String.valueOf(Character.toUpperCase(sb.charAt(0))));
        sb.insert(0, "get");
        String target = sb.toString();
        return object.getClass().getMethod(target).invoke(object);

    }


    private static StringBuilder divideByList(String template, Object object) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pattern pattern = Pattern.compile(startRegex);
        Matcher matcher = pattern.matcher(template);
        StringBuilder sb = new StringBuilder();
        // Check all occurrences
        if(matcher.find() != false){
            Pattern pattern1 = Pattern.compile(endRegex);
            Matcher matcher1 = pattern.matcher(template);
            String tmp = matcher.group();
            String target = tmp.substring(3, tmp.length()-2);

            //first part
            sb.append(renderObjectValues(template.substring(0, matcher.start()), object));
            String middleTemplate = template.substring(matcher.end(), matcher1.start());

            //middle part
            List list = (List) getValue(object,target);
            for(int i=0; i<list.size(); i++)
                sb.append(divideByList(middleTemplate, list.get(i)));

            //last part
            return sb.append(divideByList(template.substring(matcher.end()), object));
        }
        return sb.append(renderObjectValues(template, object));
    }



    private static StringBuilder renderObjectValues(String template, Object object) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile(valueRegex);
        Matcher matcher = pattern.matcher(template);
        int prevEnd = 0;
        while (matcher.find()) {
            String tmp = matcher.group();
            String target = tmp.substring(3, tmp.length()-2);
            sb.append(template, prevEnd, matcher.start());
            sb.append(getValue(object, target).toString());
            prevEnd = matcher.end();
        }
        return sb.append(template.substring(prevEnd));
    }

}
