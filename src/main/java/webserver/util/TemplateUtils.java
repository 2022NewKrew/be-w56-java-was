package webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.domain.Model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TemplateUtils {
    public static final Logger log = LoggerFactory.getLogger(TemplateUtils.class);
    private static Object getAttribute(Object obj, String name) throws NoSuchFieldException, IllegalAccessException {
        if (obj instanceof Model)
            return ((Model) obj).getAttribute(name);

        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static String convertTemplate(String fileString, Object obj) throws NoSuchFieldException, IllegalAccessException, IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        int endContent = 0;
        while(true) {
            int nameStart = fileString.indexOf("{{", endContent);
            int nameEnd = fileString.indexOf("}}", nameStart);
            if (nameStart == -1) break;

            resultStringBuilder.append(fileString, endContent, nameStart);
            String name = fileString.substring(nameStart + 2, nameEnd);
            endContent = insertConvertedContent(resultStringBuilder, name, nameEnd, fileString, obj);
        }

        resultStringBuilder.append(fileString, endContent, fileString.length());
        return resultStringBuilder.toString();
    }

    private static int insertConvertedContent(StringBuilder resultStringBuilder, String name, int nameEnd, String fileString, Object obj) throws NoSuchFieldException, IllegalAccessException, IOException {
        if (name.charAt(0) == '#') {
            name = name.substring(1);
            int endContent = fileString.indexOf("{{/" + name + "}}", nameEnd);
            resultStringBuilder.append(processList(fileString.substring(nameEnd + 2, endContent), getAttribute(obj, name)));
            return endContent + name.length() + 5;
        }

        resultStringBuilder.append(getAttribute(obj, name));
        return nameEnd + 2;
    }

    private static String processList(String content, Object value) throws NoSuchFieldException, IllegalAccessException, IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        List<Object> values;
        if (value instanceof List) values = (List<Object>) value;
        else {
            values = new ArrayList<>();
            values.add(value);
        }

        for (Object obj : values)
            resultStringBuilder.append(convertTemplate(content, obj));

        return resultStringBuilder.toString();
    }
}
