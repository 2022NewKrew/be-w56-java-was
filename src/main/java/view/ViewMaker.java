package view;

import com.google.common.io.Files;
import exception.NotFoundException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import view.template.ViewTemplate;

public class ViewMaker {

    public static byte[] getView(String filePath, Map<String, Object> model) {
        if (ViewMatcher.getTemplate(filePath) != null) {
            return applyModel(ViewMatcher.getTemplate(filePath), model);
        }
        return getFile("./webapp" + filePath);
    }

    public static byte[] getNotFoundView() {
        return getFile("./webapp/notFound.html");
    }

    public static byte[] getBadRequestView() {
        return getFile("./webapp/badRequest.html");
    }

    private static byte[] applyModel(ViewTemplate template, Map<String, Object> model) {
        //미구현
        StringBuilder result = new StringBuilder();
        result.append(ViewTemplate.header);
        result.append(template.getTemplate(model));
        result.append(ViewTemplate.footer);
        return result.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] getFile(String filePath) {
        try {
            return Files.toByteArray(new File(filePath));
        } catch (Exception exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }
}
