package webserver;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import webserver.web.response.HttpStatus;
import webserver.web.response.Response;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class View {

    private static final String PREFIX = "/Users/kakao/Desktop/be-w56-java-was/webapp/";
    private static final String HOST = "localhost:8080/";
    private final String viewPath;
    private final Model model;
    private final Response.ResponseBuilder builder;

    public View(String viewPath, Model model, Response.ResponseBuilder builder) {
        this.viewPath = viewPath;
        this.model = model;
        this.builder = builder;
    }

    public Response render() throws IOException {
        if (viewPath.contains("redirect")) {
            return builder.setStatus(HttpStatus.REDIRECT)
                    .setRedirectLocation(viewPath.split(":")[1])
                    .build();
        }
        File file = new File(PREFIX + viewPath);
        byte[] result = forward(file).getBytes(StandardCharsets.UTF_8);
        return builder.setStatus(HttpStatus.OK)
                .setResult(result)
                .setContentLength(result.length)
                .setContentType(new Tika().detect(file))
                .build();
    }

    private String forward(File file) throws IOException {
        String stringFile = Files.readString(file.toPath());
        return replaceConditions(stringFile);
    }

    private String replaceConditions(String stringFile) {
        Pattern pattern = Pattern.compile("\\{\\{#(.|\n)*\\{\\{\\/.*\\}\\}");
        Matcher matched = pattern.matcher(stringFile);
        while (matched.find()) {
            String block = matched.group();
            String changedBlock = changeSingleBlock(block);
            stringFile = stringFile.replace(block, changedBlock);
        }
        return stringFile;
    }

    private String changeSingleBlock(String block) {
        String attributeKeyword = getTargetAttributeKeyword(block);
        Object attribute = model.getAttribute(attributeKeyword);
        block = block.replaceAll("\\{\\{[#/].*\\}\\}", "");
        if (attribute == null)
            return "";
        if (attribute.getClass().equals(ArrayList.class)) {
            return changeAllAttribute(block, (List<?>) attribute);
        }

        return "";
    }

    private String getTargetAttributeKeyword(String block) {
        Pattern pattern = Pattern.compile("\\{\\{#.*\\}\\}");
        Matcher matcher = pattern.matcher(block);
        String group = "";
        while (matcher.find()) {
            group = matcher.group();
        }
        return group.replace("{{#", "").replace("}}", "");
    }

    private String changeAllAttribute(String block, List<?> results) {
        StringBuilder sb = new StringBuilder();

        List<String> variables = getAllVariables(block);

        results.forEach(result -> sb.append(changeLine(block, variables, result)));
        return sb.toString();
    }

    private List<String> getAllVariables(String block) {
        List<String> variables = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{[^#/]*\\}").matcher(block);
        while (matcher.find()) {
            variables.add(matcher.group().replace("{", "").replace("}", ""));
        }
        return variables;
    }

    private String changeLine(String block, List<String> variables, Object result) {
        String changed = block;
        for (String variable : variables) {
            changed = changeLineAttribute(result, changed, variable);
        }
        return changed;
    }

    private String changeLineAttribute(Object result, String changed, String variable) {
        try {
            Field field = result.getClass().getDeclaredField(variable);
            field.setAccessible(true);
            changed = changed.replace("{" + variable + "}", field.get(result).toString());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error(e.getMessage());
        }
        return changed;
    }
}
