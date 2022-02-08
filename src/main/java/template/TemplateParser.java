package template;

import exception.InternalErrorException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParser {
    private static final String LOOP_START_REGEX = "\\{\\{#[a-zA-Z]+}}";
    private static final String VALUE_REGEX = "\\{\\{[a-zA-Z]+}}";

    public static byte[] parse(String viewFilePath, Map<String, Object> models) {
        try {
            String templateString = Files.readString(Path.of(viewFilePath));
            String renderedViewString = parseInternal(templateString, models, null);

            return renderedViewString.getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }

    private static String parseInternal(String templateString, Map<String, Object> models, Object context) {
        Pattern loopStartPattern = Pattern.compile(LOOP_START_REGEX);
        Matcher loopMatcher = loopStartPattern.matcher(templateString);
        while (loopMatcher.find()) {
            String loopStartTag = loopMatcher.group();
            String iterableName = loopStartTag.substring(3, loopStartTag.length() - 2);
            String expectedLoopEndTag = loopStartTag.replaceFirst("#", "/");

            int loopStartIndex = templateString.indexOf(loopStartTag);
            int loopEndIndex = templateString.indexOf(expectedLoopEndTag) + expectedLoopEndTag.length();

            String loopTemplate = templateString.substring(loopStartIndex, loopEndIndex);
            String templateInsideLoop = loopTemplate.substring(loopStartTag.length(), loopTemplate.length() - expectedLoopEndTag.length());

            Iterable<?> iterableObject = getIterableObject(models, context, iterableName);

            StringBuilder parsedLoopTemplateBuilder = new StringBuilder();
            for (Object s : iterableObject) {
                parsedLoopTemplateBuilder.append(parseInternal(templateInsideLoop, models, s));
            }

            templateString = templateString.replace(loopTemplate, parsedLoopTemplateBuilder);
            System.out.println(templateString);
            loopMatcher = loopStartPattern.matcher(templateString);
        }

        Pattern valuePattern = Pattern.compile(VALUE_REGEX);
        Matcher valueMatcher = valuePattern.matcher(templateString);

        while (valueMatcher.find()) {
            String valueTag = valueMatcher.group();
            String valueName = valueTag.substring(2, valueTag.length() - 2);

            String valueString;
            try {
                valueString = getValue(models, context, valueName).toString();
            } catch (NullPointerException e) {
                valueString = "";
            }

            templateString = templateString.replace(valueTag, valueString);
        }

        return templateString;
    }

    private static Iterable<?> getIterableObject(Map<String, Object> models, Object context, String target) {
        Object iterableObject;
        try {
            iterableObject =  context.getClass().getField(target);
        } catch (NullPointerException | NoSuchFieldException e) {
            iterableObject =  models.get(target);
        }

        if (!(iterableObject instanceof Iterable<?>)) {
            throw new InternalErrorException(null);
        }

        return (Iterable<?>) iterableObject;
    }

    private static Object getValue(Map<String, Object> models, Object context, String target) {
        try {
            StringBuilder sb = new StringBuilder(target);
            sb.replace(0, 1, String.valueOf(Character.toUpperCase(sb.charAt(0))));
            sb.insert(0, "get");
            String getterName = sb.toString();
            return context.getClass().getMethod(getterName).invoke(context);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            return models.get(target);
        }
    }
}
