package webapplication;

import webapplication.data.Model;
import webapplication.dto.ModelAndView;
import webapplication.dto.ViewRenderingResult;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewRenderer {

    private static final String PAGE_ROOT = "./webapp";

    private static final Pattern REGEX_TEMPLATE_ITER = Pattern.compile("\\{\\{\\#(\\w+)\\}\\}([\\w\\W]*)\\{\\{\\/(\\w+)\\}\\}");

    public static final Pattern REGEX_TEMPLATE_VARS = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    public static ViewRenderingResult render(String viewName, Model model) throws Exception {
        String fileFullName = PAGE_ROOT + viewName;
        String html = Files.readString(Paths.get(fileFullName));
        html = replaceTemplateIter(model, html);
        html = replaceTemplateVars(model, html);
        return new ViewRenderingResult(html.getBytes(StandardCharsets.UTF_8));
    }

    private static String replaceTemplateIter(Model model, String html) throws Exception {
        StringBuilder sb = new StringBuilder();
        Matcher iterMatcher = REGEX_TEMPLATE_ITER.matcher(html);
        while (iterMatcher.find()) {
            String iterVariableOpen = iterMatcher.group(1);
            String iterVariableClose = iterMatcher.group(3);
            if(!iterVariableOpen.equals(iterVariableClose)) {
                throw new Exception("Parsing 실패");
            }

            StringBuilder innerSb = new StringBuilder();
            String innerHtml = iterMatcher.group(2);
            Iterable<?> iterObject = (Iterable<?>)model.getAttribute(iterVariableOpen).orElseThrow(() -> new Exception("변수가 없습니다."));
            final String finalInnerHtml = innerHtml;
            iterObject.forEach(element -> {
                innerSb.append(replaceTemplateIterInner(element, finalInnerHtml));
            });
            iterMatcher.appendReplacement(sb, innerSb.toString());
        }
        return sb.toString();
    }

    private static String replaceTemplateIterInner(Object object, String html) {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = REGEX_TEMPLATE_VARS.matcher(html);
        while (matcher.find()) {
            String varName = matcher.group(1);
            String value = getField(object, varName);
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

    private static String replaceTemplateVars(Model model, String html) throws Exception {
        StringBuilder sb = new StringBuilder();
        Matcher matcher = REGEX_TEMPLATE_VARS.matcher(html);
        while (matcher.find()) {
            String varName = matcher.group(1);
            String value = (String) model.getAttribute(varName).orElseThrow(() -> new Exception("변수가 없습니다."));
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        html = sb.toString();
        return html;
    }
}
