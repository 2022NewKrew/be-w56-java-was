package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class HtmlUtils {

    private static final String START_TEXT = "{{";
    private static final String END_TEXT = "}}";
    private static final String LIST_START_TEXT = "#";
    private static final String LIST_END_TEXT = "/";

    public static byte[] getBytes(Map<String, Object> model, String path) throws IOException, NoSuchFieldException, IllegalAccessException {

        String viewHtml = Files.readString(Path.of(path));
        int fromIdx = 0;
        int strIdx, endIdx;
        StringBuilder sb = new StringBuilder();
        String target;
        while ((strIdx = viewHtml.indexOf(START_TEXT, fromIdx)) != -1) {
            endIdx = viewHtml.indexOf(END_TEXT, strIdx);
            target = viewHtml.substring(strIdx+2, endIdx);
            sb.append(viewHtml, fromIdx, strIdx);
            if (target.startsWith(LIST_START_TEXT)) {
                strIdx = viewHtml.indexOf(START_TEXT+LIST_END_TEXT, endIdx);
                sb.append(makeIterBlock(model.get(target.substring(1)), viewHtml.substring(endIdx+2, strIdx)));
                fromIdx = strIdx+2+target.length()+2;
            } else {
                sb.append(model.get(target));
                fromIdx = endIdx+2;
            }
        }
        sb.append(viewHtml, fromIdx, viewHtml.length());

        return sb.toString().getBytes();
    }

    protected static StringBuilder makeIterBlock(Object object, String view) throws NoSuchFieldException, IllegalAccessException {
        if (!(object instanceof List)) {
            return null;
        }
        List<?> objects = (List<?>) object;
        StringBuilder iterBlock = new StringBuilder();
        for (Object obj : objects) {
            iterBlock.append(fillBlock(obj, view));
        }
        return iterBlock;
    }

    protected static StringBuilder fillBlock(Object object, String view) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        int fromIdx = 0;
        int strIdx, endIdx;
        StringBuilder sb = new StringBuilder();
        String target;
        Field[] dfs = clazz.getDeclaredFields();
        while ((strIdx = view.indexOf(START_TEXT, fromIdx)) != -1) {
            endIdx = view.indexOf(END_TEXT, strIdx);
            target = view.substring(strIdx+2, endIdx);
            sb.append(view.substring(fromIdx, strIdx));
            sb.append(getValue(object, target, dfs));
            fromIdx = endIdx+2;
        }
        sb.append(view.substring(fromIdx, view.length()));
        return sb;
    }

    private static String getValue(Object object, String name, Field[] dfs) throws IllegalAccessException {
        for (Field field : dfs) {
            if (field.getName().equals(name)) {
                field.setAccessible(true);
                return field.get(object).toString();
            }
        }
        return "";
    }

}
