package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ui.Model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateEngine.class);
    // Todo 정규표현식 공부 좀 더 해야할 듯
    private static final String startRegex = "\\{\\{\\#[a-zA-Z]+\\}\\}";
    private static final String endRegex = "\\{\\{\\/[a-zA-Z]+\\}\\}";
    private static final String valueRegex = "\\{\\{[a-zA-Z]*\\.*[a-zA-Z]+\\}\\}";

    public static byte[] render(String template, Model model) throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String templateString = Files.readString(Path.of("./webapp" + template));
        return divideByList(templateString, model).toString().getBytes(StandardCharsets.UTF_8);
    }

    private static Object getValue(Object object, String key) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (key.contains("."))
            return getObjectAttribute(object, key);
        if (Model.class.isAssignableFrom(object.getClass()))
            return getModelValue(object, key);
        return getObjectValue(object, key);

    }

    private static Object getObjectAttribute(Object object, String key) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String[] keys = key.split("\\.");
        Object obj = getModelValue(object, keys[0]);
        return getObjectValue(obj, keys[1]);
    }

    private static Object getModelValue(Object object, String key) {
        return ((Model) object).getAttribute(key);
    }

    private static Object getObjectValue(Object object, String key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder(key);
        sb.replace(0, 1, String.valueOf(Character.toUpperCase(sb.charAt(0))));
        sb.insert(0, "get");
        String target = sb.toString();
        LOGGER.debug("target : {} ", target);
        return object.getClass().getMethod(target).invoke(object);
    }


    public static StringBuilder divideByList(String template, Object object) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pattern pattern = Pattern.compile(startRegex);
        Matcher matcher = pattern.matcher(template);
        StringBuilder sb = new StringBuilder();
        // Check all occurrences
        if(matcher.find() != false){
            Pattern pattern1 = Pattern.compile(endRegex);
            Matcher matcher1 = pattern1.matcher(template);
            // Todo find() 먼저 해주어야 한다.
            if(matcher1.find() == false)
                throw new RuntimeException("템플릿이 잘못되었습니다!");
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
            return sb.append(divideByList(template.substring(matcher1.end()), object));
        }
        return sb.append(renderObjectValues(template, object));
    }



    private static StringBuilder renderObjectValues(String template, Object object) throws NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile(valueRegex);
        Matcher matcher = pattern.matcher(template);
        int prevEnd = 0;
        boolean res = false;
        while ((res = matcher.find()) == true) {
            String tmp = matcher.group();
            String target = tmp.substring(2, tmp.length() - 2);
            sb.append(template, prevEnd, matcher.start());
            sb.append(getValue(object, target).toString());
            prevEnd = matcher.end();
        }
        return sb.append(template.substring(prevEnd));
    }

}
