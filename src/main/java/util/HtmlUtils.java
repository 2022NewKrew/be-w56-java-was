package util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.List;

public class HtmlUtils {
    private static final String SYMBOL_OPEN = "{{";
    private static final String SECTION_START = SYMBOL_OPEN + "#";  // {{#
    private static final String SECTION_END = SYMBOL_OPEN + "/";    // {{/
    private static final String SYMBOL_CLOSE = "}}";

    /**
     * 동적으로 코드를 변환할 html 파일을 받아서 tag를 확인하고 데이터를 삽입 후 변환된 코드 반환
     * @param templateFile 기본 html 파일
     * @param model 삽입할 데이터
     * @return 변환 후 StringBuilder 객체
     * @throws IOException templateFile readAllBytes 실패 시
     */
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

    /**
     * code 문자열 내에서 특정 인덱스에 위치한 {{ }} 형태의 테그를 삭제해줌.
     * 인자로 받은 StringBuilder 객체 자체에서 삭제
     *
     * @param code tag 를 지우고 싶은 코드
     * @param startIndex code 에서 tag 가 시작되는 위치
     */
    private static void removeTag(StringBuilder code, int startIndex) {
        int endIndex = code.indexOf(SYMBOL_CLOSE, startIndex);

        if (startIndex < endIndex) {
            code.delete(startIndex, endIndex + SYMBOL_CLOSE.length());
        }
    }

    /**
     * List 객체인 model 을 순회하면서, 데이터가 삽입된 section 코드를 반복 생성
     *
     * @param sectionBase section 코드 (html 내에서 {{#foo}} 로 시작하고 {{/foo}} 로 끝나는 부분)
     * @param model 코드에 삽입할 데이터 List
     * @return 생성된 문자열
     */
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

    /**
     * 인자로 받은 StringBuilder 객체에서 {{fieldName}} 으로 되어있는 태그를 fieldValue 로 대체해줌
     *
     * @param section 코드 문자열
     * @param fieldName 실제 값으로 대체될 태그 이름
     * @param fieldValue 대체해서 삽입될 값
     */
    private static void replaceTagWithData(StringBuilder section, String fieldName, Object fieldValue) {
        int start = section.indexOf(fieldName);

        if (start != -1) {
            start -= SYMBOL_OPEN.length();
            removeTag(section, start);
            section.insert(start, fieldValue);
        }
    }
}
