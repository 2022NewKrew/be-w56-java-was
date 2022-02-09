package framework.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static framework.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class MustacheResolverTest {
    @Test
    @DisplayName("현재 줄에 Mustache가 존재하는지 확인")
    public void hasMustache() {
        String line = "asdasd asd asdasdasd {{&a}}asdasdasd asdasd";

        assertTrue(MustacheResolver.hasMustache(line));
    }

    @Test
    @DisplayName("현재 줄에 적절한 Mustache가 존재하는지 확인")
    public void hasMatchedMustache() {
        String loopLine = "{{#name}}";
        String elseLine = "{{^name}}";
        String endLine = "{{/name}}";
        String templateLine = "{{>name}}";

        Pattern pattern = Pattern.compile(LOOP_IF_MUSTACHE_REGEX);
        Matcher matcher = pattern.matcher(loopLine);

        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }

        assertTrue(loopLine.matches(LOOP_IF_MUSTACHE_REGEX));
        assertTrue(elseLine.matches(ELSE_MUSTACHE_REGEX));
        assertTrue(endLine.matches(END_MUSTACHE_REGEX));
        assertTrue(templateLine.matches(TEMPLATE_MUSTACHE_REGEX));
    }
}