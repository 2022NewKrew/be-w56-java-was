package http.view;

import http.exception.NotFound;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class ViewResolver {

    private static final String STATIC_FILE_PATH = "./webapp";
    private static final String TEMPLATE_FILE_EXTENSION = "vhtml"; // vince html

    public static byte[] getView(String path, Map<String, Object> model) {
        log.debug("path: {}", path);
        try {
            if (path.endsWith(TEMPLATE_FILE_EXTENSION)) {
                return getTemplateView(STATIC_FILE_PATH + path, model);
            } else {
                return getFile(STATIC_FILE_PATH + path);
            }
        } catch (IOException e) {
            throw new NotFound();
        }
    }

    // ----------------------------------------------------------------------------------------------------------

    private static byte[] getFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }

    private static byte[] getTemplateView(String path, Map<String, Object> model) throws IOException {
        Pattern pattern = (Pattern) model.get("pattern");
        String toReplaceWith = (String) model.get("toReplaceWith");

        String tmplFile = Files.readString(Paths.get(path), StandardCharsets.UTF_8);
        String replaced = pattern.matcher(tmplFile).replaceAll(toReplaceWith);
        return replaced.getBytes(StandardCharsets.UTF_8);
    }
}
