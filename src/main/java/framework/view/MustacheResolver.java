package framework.view;

import framework.util.ModelViewAttributes;
import framework.util.exception.AttributeNotFoundException;
import framework.util.exception.PropertyNotFoundException;
import framework.util.exception.StaticFileNotFoundException;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static framework.util.Constants.CONTEXT_PATH;

public class MustacheResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MustacheResolver.class);

    private static List<Byte> byteList;
    private static ModelViewAttributes attributes;
    private static BufferedReader br;

    public static byte[] readHtmlFile(String absolutePath, ModelViewAttributes modelViewAttributes) {
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath)));
            byteList = new ArrayList<>();
            attributes = modelViewAttributes;

            String line;

            while ((line = br.readLine()) != null) {
                // Mustache가 존재하므로 해당 부분 처리
                if (hasMustache(line)) {
                    handleMustache(line);
                    continue;
                }

                insertStringInContentList(line);
            }
        } catch (IOException e) {
            throw new StaticFileNotFoundException();
        }

        return listToArray();
    }

    private static boolean hasMustache(String line) {
        return line.contains("{{") && line.contains("}}") &&
                line.indexOf("{{") < line.indexOf("}}");
    }

    private static void handleMustache(String line) throws IOException {
        String trimmedLine = line.trim();
        String mustacheValue = trimmedLine.substring(3, trimmedLine.length() - 2);

        // Mustache로만 이뤄진 줄이며 해당 객체가 list형이거나 해당 객체가 존재하는지에 대한 if문 처리
        if (trimmedLine.startsWith("{{#") && trimmedLine.endsWith("}}")) {
            if (!attributes.contains(mustacheValue)) {
                while (!br.readLine().trim().equals("{{/" + mustacheValue + "}}")) {}
                return;
            }

            // 해당 Attribute를 가져옴
            Object attribute = attributes.getAttribute(mustacheValue);

            // list형
            if (attribute instanceof List<?>) {
                handleList(mustacheValue, (List<?>) attribute);
                return;
            }

            // if문
            handleIf(mustacheValue, attribute);
            return;
        }

        // Mustache로만 이뤄진 줄이며 해당 객체가 존재하는지에 대한 else문 처리
        if (trimmedLine.startsWith("{{^") && trimmedLine.endsWith("}}")) {
            if (attributes.contains(mustacheValue)) {
                while (!br.readLine().trim().equals("{{/" + mustacheValue + "}}")) {}
                return;
            }

            handleElse(mustacheValue);
            return;
        }

        // Mustache로만 이뤄진 줄이며 해당 파일을 template으로 사용
        if (trimmedLine.startsWith("{{>") && trimmedLine.endsWith("}}")) {
            // 기존 BufferedReader와 Byte를 담는 list를 저장
            BufferedReader originBr = br;
            List<Byte> originByteList = byteList;

            // Template HTML 파일을 읽음
            readHtmlFile(CONTEXT_PATH + "/" + mustacheValue + ".html", attributes);

            // BufferedReader를 다시 되돌려놓고 list는 합침
            br = originBr;
            byteList.addAll(0, originByteList);
            return;
        }

        // Mustache만으로 이뤄진게 아닌 평문과 함께 들어가있는 경우
        convertMustacheOfCurrentLine(line, MustacheCategory.OTHER);
    }

    private static void handleList(String mustacheValue, List<?> attribute) throws IOException {
        // 빈 리스트일 때는 해당 Scope를 넘긴 후 돌아가야 함
        if (attribute.isEmpty()) {
            while (!br.readLine().trim().equals("{{/" + mustacheValue + "}}")) {}
            return;
        }

        List<String> stringsForLoop = new ArrayList<>();
        String line;

        // Scope 안의 모든 line을 가져옴
        while (!(line = br.readLine()).trim().equals("{{/" + mustacheValue + "}}")) {
            stringsForLoop.add(line);
        }

        for (int index = 0, size = attribute.size(); index < size; index++) {
            for (String currLine : stringsForLoop) {
                if (hasMustache(currLine)) {
                    convertMustacheOfCurrentLine(currLine, MustacheCategory.LIST, attribute.get(index), index);
                    continue;
                }

                insertStringInContentList(currLine);
            }
        }
    }

    private static void handleIf(String mustacheValue, Object attribute) throws IOException {
        String line;

        // Scope 안의 모든 line을 가져옴
        while (!(line = br.readLine()).trim().equals("{{/" + mustacheValue + "}}")) {
            if (hasMustache(line)) {
                convertMustacheOfCurrentLine(line, MustacheCategory.THIS_OR_GETTER, attribute);
                continue;
            }

            insertStringInContentList(line);
        }
    }

    private static void handleElse(String mustacheValue) throws IOException {
        String line;

        // Scope 안의 모든 line을 가져옴
        while (!(line = br.readLine()).trim().equals("{{/" + mustacheValue + "}}")) {
            if (hasMustache(line)) {
                convertMustacheOfCurrentLine(line, MustacheCategory.OTHER);
                continue;
            }

            insertStringInContentList(line);
        }
    }

    private static void convertMustacheOfCurrentLine(String currLine, MustacheCategory mustacheCategory, Object... objects) {
        int fromIdx = 0;
        int mustacheStartIdx = currLine.indexOf("{{", fromIdx);
        int mustacheEndIdx = currLine.indexOf("}}", mustacheStartIdx) + 2;
        StringBuilder sb = new StringBuilder();

        while (mustacheStartIdx != -1) {
            String mustacheValue = currLine.substring(mustacheStartIdx + 2, mustacheEndIdx - 2);

            try {
                if (mustacheCategory == MustacheCategory.LIST) {
                    Object attribute = objects[0];
                    int index = (int) objects[1];

                    convertForList(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue, attribute, index);
                    continue;
                }

                if (mustacheCategory == MustacheCategory.THIS_OR_GETTER) {
                    Object attribute = objects[0];

                    convertThisOrGetter(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue, attribute);
                    continue;
                }

                convertOther(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue);
            } finally {
                // 첨자 이동
                fromIdx = mustacheEndIdx;
                mustacheStartIdx = currLine.indexOf("{{", fromIdx);
                mustacheEndIdx = currLine.indexOf("}}", mustacheStartIdx) + 2;
            }
        }

        if (fromIdx != currLine.length()) {
            sb.append(currLine, fromIdx, currLine.length());
        }

        insertStringInContentList(sb.toString());
    }

    private static void convertForList(StringBuilder sb, String currLine, int fromIdx, int mustacheStartIdx,
                                       String mustacheValue, Object attribute, int index) {
        // Mustache 안에 '#index'라고 돼있는 경우, loop의 index를 가져와서 넣음
        if ("#index".equals(mustacheValue) && index >= 0) {
            sb.append(currLine, fromIdx, mustacheStartIdx).append(index + 1);
            return;
        }

        convertThisOrGetter(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue, attribute);
    }

    private static void convertThisOrGetter(StringBuilder sb, String currLine, int fromIdx, int mustacheStartIdx,
                                            String mustacheValue, Object attribute) {
        // Mustache 안에 'this'라고 돼있는 경우, 현재 객체를 가져와서 넣음
        if ("this".equals(mustacheValue)) {
            sb.append(currLine, fromIdx, mustacheStartIdx).append(attribute);
            return;
        }

        invokeGetter(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue, attribute);
    }

    private static void invokeGetter(StringBuilder sb, String currLine, int fromIdx, int mustacheStartIdx,
                                     String mustacheValue, Object attribute) {
        // this.XXX 또는 (mustache 값).XXX로 오면 XXX를 property 이름으로 간주
        if (mustacheValue.startsWith("this.") || mustacheValue.startsWith(mustacheValue + ".")) {
            mustacheValue = mustacheValue.split("\\.")[1];
        }

        try {
            Method getter = attribute.getClass().getMethod("get" + mustacheValue.substring(0, 1).toUpperCase(Locale.ROOT) + mustacheValue.substring(1));
            Object returned = getter.invoke(attribute);

            sb.append(currLine, fromIdx, mustacheStartIdx).append(returned.toString());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new PropertyNotFoundException(mustacheValue);
        }
    }

    private static void convertOther(StringBuilder sb, String currLine, int fromIdx, int mustacheStartIdx, String mustacheValue) {
        if (!attributes.contains(mustacheValue)) {
            throw new AttributeNotFoundException(mustacheValue);
        }

        Object attribute = attributes.getAttribute(mustacheValue);

        sb.append(currLine, fromIdx, mustacheStartIdx).append(attribute.toString());
    }

    private static void insertStringInContentList(String line) {
        for (byte currByte : StringUtils.getBytesUtf8(line)) {
            byteList.add(currByte);
        }

        // 개행 추가
        byteList.add(StringUtils.getBytesUtf8("\n")[0]);
    }

    public static byte[] listToArray() {
        int size = byteList.size();
        byte[] byteArray = new byte[size];

        for (int i = 0; i < size; i++) {
            byteArray[i] = byteList.get(i);
        }

        return byteArray;
    }

    private enum MustacheCategory {
        LIST, THIS_OR_GETTER, OTHER
    }
}
