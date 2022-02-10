package template;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtils {

    private static final String START_REGEX = "\\{\\{#(.*)}}((.|\n)*)\\{\\{/(.*)}}";
    private static final int KEY_GROUP_INDEX = 1;
    private static final int INNER_CONTENT_INDEX = 2;
    private static final String CONTENT_REGEX = "\\{\\{(\\w+)}}";

    public static String mappingModel(String contents, Model model) {
        Pattern pattern = Pattern.compile(START_REGEX);
        Matcher matcher = pattern.matcher(contents);

        if (!matcher.find()) {
            return contents;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(contents, 0, matcher.start(0));
        String key = matcher.group(KEY_GROUP_INDEX);

        Object modelValues = model.getMap().get(key);
        if (!(modelValues instanceof Iterable)) {
            return contents;
        }

        String subContents = contents.substring(
            matcher.start(INNER_CONTENT_INDEX),
            matcher.end(INNER_CONTENT_INDEX));
        ((Iterable<?>) modelValues).forEach(value -> builder.append(mapping(subContents, value)));
        builder.append(contents.substring(matcher.end()));
        return builder.toString();
    }

    private static String mapping(String content, Object value) {
        try {
            Pattern fieldPattern = Pattern.compile(CONTENT_REGEX);
            Matcher matcher = fieldPattern.matcher(content);

            int startIndex = 0;
            StringBuilder builder = new StringBuilder();

            while (matcher.find()) {
                String field = matcher.group(KEY_GROUP_INDEX);
                Field declaredField = value.getClass().getDeclaredField(field);
                declaredField.setAccessible(true);
                builder.append(content, startIndex, matcher.start());
                builder.append(declaredField.get(value));
                startIndex = matcher.end();
            }
            builder.append(content.substring(startIndex));
            return builder.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return content;
    }

}
