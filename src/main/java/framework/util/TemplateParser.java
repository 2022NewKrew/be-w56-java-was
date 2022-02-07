package framework.util;

import framework.params.Model;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TemplateParser {

    private final String INLINE_PATTERN = ".*\\{\\{.+}}.*";
    private final String BLOCK_OPEN_PATTERN = ".*\\{\\{#.+}}.*";
    private final String BLOCK_CLOSE_PATTERN = ".*\\{\\{\\/.+}}.*";
    private final String NEW_LINE = "\r\n";

    public byte[] getModelAppliedView(File file, Model model) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            if (line.matches(BLOCK_OPEN_PATTERN)) {
                line = replaceBlockTemplate(line, br, model);
            } else if (line.matches(INLINE_PATTERN)) {
                line = replaceInlineTemplate(line, model);
            }
            builder.append(line).append(NEW_LINE);
        }
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String replaceInlineTemplate(String line, Model model) {
        int idx = 0;
        while (idx < line.length()) {
            String target = getTemplateComponentName(line, idx, false);
            if (target.length() <= 0) break;
            idx = line.indexOf("}}", idx) + 1;
            line = replaceTemplateComponent(line, target, model.getAttributes(target).toString());
        }
        return line;
    }

    private String replaceBlockTemplate(String line, BufferedReader br, Model model) throws IOException {
        String targetModelName = getTemplateComponentName(line, 0, true);
        String blockLines = getAllHtmlElementsWrappedAsBlock(line, br);

        // model에 key, value가 있을 경우만 렌더링, 없으면 해당 부분 렌더링 하지 않음
        Object target;
        try {
            target = model.getAttributes(targetModelName);
            checkModelAttributeExist(target);
        } catch (Exception e) {
            return "";
        }

        // list obj
        if (target instanceof List) {
            return getTableHtmlElements(model, (List<?>) target, blockLines);
        }
        // non-list obj
        else {
            return replaceAllTemplateComponent(model, target, blockLines);
        }
    }

    private String getTableHtmlElements(Model model, List<?> targetList, String blockLines) {
        StringBuilder builder = new StringBuilder();
        for (var item : targetList) {
            builder.append(replaceAllTemplateComponent(model, item, blockLines));
        }
        return builder.toString();
    }

    private String replaceAllTemplateComponent(Model model, Object targetObj, String blockLines) {
        int idx = 0;
        while (idx < blockLines.length()) {
            String targetFieldName = getTemplateComponentName(blockLines, idx, false);
            if (targetFieldName.length() <= 0) break;
            blockLines = replaceEachTemplateComponent(model, targetObj, blockLines, targetFieldName);
            idx = blockLines.indexOf("}}", idx) + 1;
        }
        return blockLines;
    }

    /**
     * target object에 해당하는 field가 없다면, model에 해당하는 값이 있는지 확인
     * @param model controller로부터 받은 model
     * @param targetObj 현재 템플릿 블럭이 참조하고 있는 오브젝트, 예를들어 {{#user}}라면 UserDto 객체
     * @param blockLines 현재 템플릿 블럭으로 감싸여져있는 모든 html 내용물
     * @param targetFieldName 타겟 오브젝트로부터 가져오고자 하는 필드 이름
     * @return targetFieldName이 치환된 String 값을 리턴
     */
    private String replaceEachTemplateComponent(Model model, Object targetObj, String blockLines, String targetFieldName) {
        Object replacementObj;
        try {
            replacementObj = getFieldValue(targetObj, targetFieldName);
        } catch (Exception e) {
            replacementObj = model.getAttributes(targetFieldName);
        }
        checkModelAttributeExist(replacementObj);
        return replaceTemplateComponent(blockLines, targetFieldName, replacementObj.toString());
    }

    private Object getFieldValue(Object targetObj, String targetFieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder getterMethodNameBuilder = new StringBuilder();
        getterMethodNameBuilder.append("get").append(targetFieldName);
        getterMethodNameBuilder.setCharAt(3, (char) (getterMethodNameBuilder.charAt(3) - ('a' - 'A')));
        Method getter = targetObj.getClass().getMethod(getterMethodNameBuilder.toString());
        return getter.invoke(targetObj, (Object[]) null);
    }

    private String getTemplateComponentName(String line, int offset, boolean isBlockTemplate) {
        int startIdx = line.indexOf("{{", offset);
        int endIdx = line.indexOf("}}", offset);
        if (startIdx < 0) return "";
        startIdx += isBlockTemplate ? 3 : 2;
        return line.substring(startIdx, endIdx);
    }

    private String getAllHtmlElementsWrappedAsBlock(String line, BufferedReader br) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(line.replaceAll(BLOCK_OPEN_PATTERN, "")).append(NEW_LINE);
        while (!line.matches(BLOCK_CLOSE_PATTERN) && (line = br.readLine()) != null) {
            builder.append(line).append(NEW_LINE);
        }
        return builder.toString().replaceAll(BLOCK_CLOSE_PATTERN, "");
    }

    private String replaceTemplateComponent(String line, String target, String replacement) {
        target = String.format("{{%s}}", target);
        return line.replace(target, replacement);
    }

    private void checkModelAttributeExist(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("템플릿 엔진 에러: 모델을 찾을 수 없습니다.");
        }
    }
}
