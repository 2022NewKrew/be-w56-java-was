package template;

import app.user.exception.NonIterableException;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateUtils {

    private static final Logger logger = LoggerFactory.getLogger(TemplateUtils.class);
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
        Iterable<?> attributeValue = getAttributeValue(matcher, model);

        String startContents = contents.substring(0, matcher.start());
        String subContents = contents.substring(
            matcher.start(INNER_CONTENT_INDEX),
            matcher.end(INNER_CONTENT_INDEX));
        String endContents = contents.substring(matcher.end());

        StringBuilder builder = new StringBuilder();
        builder.append(startContents);
        attributeValue.forEach(value -> builder.append(mappingValue(subContents, value)));
        builder.append(endContents);
        return builder.toString();
    }

    private static String mappingValue(String content, Object value) {
        try {
            Pattern fieldPattern = Pattern.compile(CONTENT_REGEX);
            Matcher matcher = fieldPattern.matcher(content);
            StringBuilder builder = new StringBuilder();

            int startIndex = 0;
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return content;
    }

    private static Iterable<?> getAttributeValue(Matcher matcher, Model model) {
        String key = matcher.group(KEY_GROUP_INDEX);
        Object value = model.getMap().get(key);
        if (!(value instanceof Iterable)) {
            throw new NonIterableException();
        }
        return (Iterable<?>) value;
    }

}
