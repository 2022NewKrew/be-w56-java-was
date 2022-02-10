package webserver.template;

import static util.Constant.DOT;
import static util.Constant.REGEX_DOT;
import static util.Constant.MESSAGE_ILLEGAL_HTML_FORM;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateEngine {
    private static final Logger log = LoggerFactory.getLogger(TemplateEngine.class);
    private static final Pattern REGEX_SESSION = Pattern.compile("\\{\\{#(\\w+)\\}\\}(((?!\\{\\{#)[\\W\\w])*)\\{\\{\\/(\\w+)\\}\\}");
    private static final Pattern REGEX_VARS = Pattern.compile("\\{\\{([\\._a-zA-Z0-9\\s]+)\\}\\}");

    public static String render(String html, Model model) {
        html = renderSessions(html, model);
        return renderVars(html, model);
    }

    private static String renderSessions(String html, Model model) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = REGEX_SESSION.matcher(html);
        while (matcher.find()) {
            StringBuilder sessionSb = new StringBuilder();
            String var = matcher.group(1);
            String sessionHtml = matcher.group(2);
            Object session = model.getAttribute(var);
            if(session instanceof Collection) {
                ((Collection<?>) session).forEach(obj -> {
                    String inner = renderSessionVars(obj, sessionHtml);
                    sessionSb.append(inner);
                });
            }else {
                String inner = renderSessionVars(session, sessionHtml);
                sessionSb.append(inner);
            }
            matcher.appendReplacement(sb, sessionSb.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    private static String renderSessionVars(Object obj, String html) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = REGEX_VARS.matcher(html);
        while (matcher.find()) {
            String var = matcher.group(1);
            if(var.contains(DOT)) {
                continue;
            }
            String value = getField(obj, var);
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String getField(Object obj, String varName) {
        try {
            Field field = obj.getClass().getDeclaredField(varName);
            field.setAccessible(true);
            return field.get(obj).toString();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException();
        }
    }

    private static String renderVars(String html, Model model) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = REGEX_VARS.matcher(html);
        while (matcher.find()) {
            String var = matcher.group(1);
            String[] varTokens = var.split(REGEX_DOT);
            if(varTokens.length > 2) {
                throw new RuntimeException(MESSAGE_ILLEGAL_HTML_FORM);
            }else if(varTokens.length == 1) {
                matcher.appendReplacement(sb, (String) model.getAttribute(var));
            }else {
                String value = getField(model.getAttribute(varTokens[0]), varTokens[1]);
                matcher.appendReplacement(sb, value);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
