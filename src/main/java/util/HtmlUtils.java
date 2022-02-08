package util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.List;

public class HtmlUtils {
    private static final String SECTION_OPEN = "{{";
    private static final String SECTION_START = SECTION_OPEN + "#";  // {{#
    private static final String SECTION_END = SECTION_OPEN + "/";    // {{/
    private static final String SECTION_CLOSE = "}}";


    public static StringBuilder renderTemplate(File templateFile, Object model) throws IOException {
        byte[] bytes = Files.readAllBytes(templateFile.toPath());
        String templateStr = new String(bytes);
        StringBuilder sb = new StringBuilder(templateStr);

        int start = sb.indexOf(SECTION_START);
        if (start != -1) { // Section 존재
            removeTag(sb, start);

            int end = sb.indexOf(SECTION_END);
            removeTag(sb, end);

            StringBuilder renderedSection = renderSection(sb.substring(start, end), model);
            sb.replace(start, end, renderedSection.toString());
        }
        return sb;
    }

    private static void removeTag(StringBuilder sb, int startIndex) {
        int endIndex = sb.indexOf(SECTION_CLOSE, startIndex);

        if (startIndex < endIndex) {
            sb.delete(startIndex, endIndex + SECTION_CLOSE.length());
        }
    }

    private static StringBuilder renderSection(String sectionBase, Object model) {
        StringBuilder renderedSection = new StringBuilder();
        List<Object> models = (List<Object>) model;
        for (Object m : models) {
            try {
                StringBuilder sb = new StringBuilder(sectionBase);
                Field[] fields = m.getClass().getDeclaredFields();
                for(Field field : fields) {
                    field.setAccessible(true);
                    replaceTagWithData(sb, field.getName(), field.get(m));
                }
                renderedSection.append(sb);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return renderedSection;
    }

    private static void replaceTagWithData(StringBuilder section, String fieldName, Object fieldValue) {
        int start = section.indexOf(fieldName);

        if (start != -1) {
            start -= SECTION_OPEN.length();
            removeTag(section, start);
            section.insert(start, fieldValue);
        }
    }
}
