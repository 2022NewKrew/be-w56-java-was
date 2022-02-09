package webserver;

import webserver.model.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewRenderer {
    private static final String DEFAULT_PATH = "./webapp";
    private static final String MODEL_PATTERN = "\\{\\{[a-zA-Z]*}}";
    private static final String ITER_START_PATTERN = "\\{\\{#[a-zA-Z]*}}";
    private static final String ITER_END_PATTERN = "\\{\\{/[a-zA-Z]*}}";

    private static final Pattern modelPattern = Pattern.compile(MODEL_PATTERN);
    private static final Pattern iterStartPattern = Pattern.compile(ITER_START_PATTERN);
    private static final Pattern iterEndPattern = Pattern.compile(ITER_END_PATTERN);

    public static byte[] render(ModelAndView modelAndView) throws IOException {

        String path = modelAndView.getViewName();

        StringBuilder response = new StringBuilder();
        List<String> iterLines = new ArrayList<>();
        List<String> markdownLines = Files.readAllLines(new File(DEFAULT_PATH + path).toPath());

        boolean beforeIterStart = true;
        String key = null;

        for (String markdownLine : markdownLines) {
            if (beforeIterStart) {
                Matcher m = iterStartPattern.matcher(markdownLine);
                if (m.find()) {
                    String found = m.group();
                    key = found.substring(3, found.length() - 2);
                    beforeIterStart = false;
                }
                else {
                    response.append(markdownLine);
                    response.append("\r\n");
                }
            }
            else {
                Matcher m = iterEndPattern.matcher(markdownLine);
                if (m.find()) {
                    response.append(iterAndFillModel(key, iterLines, modelAndView));
                    response.append("\r\n");

                    beforeIterStart = true;
                }
                else
                    iterLines.add(markdownLine);
            }
        }

        return response.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static String iterAndFillModel(String key, List<String> iterLines, ModelAndView modelAndView) {
        StringBuilder response = new StringBuilder();
        for (Map<String, Object> object : (List<Map<String, Object>>) modelAndView.getModel().get(key)) {
            for (String iterLine : iterLines) {
                Matcher m = modelPattern.matcher(iterLine);
                while(m.find())
                    m.appendReplacement(response, fillModel(object, m.group()));
                m.appendTail(response);
                response.append("\r\n");
            }
        }
        return response.toString();
    }

    private static String fillModel(Map<String, Object> model, String patternFound) {
        String key = patternFound.substring(2, patternFound.length() - 2);
        System.out.println(key);
        return (String) model.get(key);
    }
}
