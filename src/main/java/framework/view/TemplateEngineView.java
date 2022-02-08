package framework.view;

import framework.http.HttpConst;
import framework.http.HttpRequest;
import framework.http.HttpResponse;
import framework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngineView implements View {
    private static final int VARIABLE_INDEX = 1;
    private static final String VARIABLE_REGEX = "\\{\\{([A-z]+)}}";
    private static final String OPEN_LOOP_REGEX = "\\{\\{#([A-z]+)}}";
    private static final String LOOP_REGEX = ".*\\{\\{#([A-z]+)}}.*";
    private static final String CLOSE_LOOP_REGEX = "\\{\\{/([A-z]+)}}";
    private final String viewName;

    public TemplateEngineView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, Object> model, HttpRequest request, HttpResponse response) {
        try {
            String html = parseHtmlTemplate(model);
            byte[] body = html.getBytes(StandardCharsets.UTF_8);
            response.setBody(body);
            if (response.getStatus() == null) {
                response.setStatus(HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.NOT_FOUND);
            new InternalResourceView(HttpConst.ERROR_PAGE).render(model, request, response);
        }

    }

    public String parseHtmlTemplate(Map<String, Object> model) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(HttpConst.STATIC_ROOT + viewName));
        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = br.readLine()) != null) {
            if (line.matches(LOOP_REGEX)) {
                sb.append(parseLoopRegex(line, br, model));
            } else {
                sb.append(parseModelVariableRegex(line, model));
            }
        }

        return sb.toString();
    }

    private String parseLoopRegex(String line, BufferedReader br, Map<String, Object> model) throws IOException {
        StringBuilder loopHtml = new StringBuilder();
        StringBuilder matchHtml = new StringBuilder();

        String target = parseTemplateVariable(line, true);

        Object values = model.get(target);
        String str;

        while ((str = br.readLine()) != null) {
            if (str.matches(CLOSE_LOOP_REGEX)) {
                break;
            }

            loopHtml.append(str).append("\n");
        }

        if (values instanceof List) {
            for (var value : (List<?>) values) {
                matchHtml.append(parseLoopVariableRegex(loopHtml.toString(), value));
            }
        } else {
            matchHtml.append(parseLoopVariableRegex(loopHtml.toString(), values));
        }

        return matchHtml.toString();
    }

    private String parseModelVariableRegex(String line, Map<String, Object> model) {
        Pattern p = Pattern.compile(VARIABLE_REGEX);
        Matcher matcher = p.matcher(line);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String variable = parseTemplateVariable(matcher.group(), false);
            if (!model.containsKey(variable)) {
                throw new IllegalArgumentException();
            }

            matcher.appendReplacement(sb, model.get(variable).toString());
        }

        if (sb.length() == 0) {
            return line;
        }

        return sb.toString();
    }

    private String parseLoopVariableRegex(String line, Object model) {
        Pattern p = Pattern.compile(VARIABLE_REGEX);
        Matcher matcher = p.matcher(line);
        StringBuilder sb = new StringBuilder();

        try {
            while (matcher.find()) {
                String variable = parseTemplateVariable(matcher.group(), false);
                String objectValue = findGetterMethod(model, variable);
                matcher.appendReplacement(sb, objectValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("메소드가 존재하지 않습니다.");
        }

        return sb.toString();
    }

    private String findGetterMethod(Object model, String variable) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String getMethod = getterMethodName(variable);
        Method method = model.getClass().getMethod(getMethod);
        Object getValue = method.invoke(model);

        return getValue.toString();
    }

    private String getterMethodName(String variable) {
        return "get" +
                variable.substring(0, 1).toUpperCase() +
                variable.substring(1);
    }

    private String parseTemplateVariable(String variable, boolean isLoop) {
        String regex = isLoop ? OPEN_LOOP_REGEX : VARIABLE_REGEX;

        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(variable);

        if (matcher.find()) {
            return matcher.group(VARIABLE_INDEX);
        }

        return "";
    }

}
