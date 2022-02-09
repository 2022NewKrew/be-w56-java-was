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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static framework.util.Constants.*;

/**
 * Mustache ({{, }})를 변환해주는 메소드들을 담은 클래스
 */
public class MustacheResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MustacheResolver.class);

    private static List<Byte> byteList;
    private static ModelViewAttributes attributes;
    private static BufferedReader br;

    /**
     * 받은 절대 경로의 HTML 파일을 읽어 모든 Mustache 부분을 변환해주는 메소드
     * @param absolutePath HTML 파일의 절대 경로
     * @param modelViewAttributes ModelView의 Attribute들
     * @return byte로 변환된 HTML 내용
     */
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

    /**
     * 현재 줄에 Mustache가 있는지 확인해주는 메소드
     * @param line 현재 줄
     * @return Mustache 존재 여부
     */
    public static boolean hasMustache(String line) {
        // {{, }}가 함께 있고 {{ 보다 }}가 더 먼저 있는 경우, Mustache가 있다고 판단
//        return line.contains("{{") && line.contains("}}") &&
//                line.indexOf("{{") < line.indexOf("}}");
        return line.matches(HAS_MUSTACHE_REGEX);
    }

    /**
     * 현재 줄의 Mustache를 변환해주는 메소드
     * @param line 현재 줄
     * @throws IOException BufferedReader에서 파일 내용을 제대로 못읽을 때 발생하는 예외
     */
    private static void handleMustache(String line) throws IOException {
        // Mustache로만 이뤄진 줄이며 해당 객체가 list형이거나 해당 객체가 존재하는지에 대한 if문 처리
        if (line.matches(LOOP_IF_MUSTACHE_REGEX)) {
            String mustacheValue = extractMustacheValue(LOOP_IF_MUSTACHE_REGEX, line);

            // 만약 ModelView의 Attribute에 포함되지 않은 값이라면 현재 Scope를 넘김
            if (!attributes.contains(mustacheValue)) {
                while (isNotEndMustache(mustacheValue, br.readLine())) {}
                return;
            }

            // 해당 Attribute를 가져옴
            Object attribute = attributes.getAttribute(mustacheValue);

            // list형 처리
            if (attribute instanceof List<?>) {
                handleList(mustacheValue, (List<?>) attribute);
                return;
            }

            // if문 처리
            handleIf(mustacheValue, attribute);
            return;
        }

        // Mustache로만 이뤄진 줄이며 해당 객체가 존재하는지에 대한 else문 처리
        if (line.matches(ELSE_MUSTACHE_REGEX)) {
            String mustacheValue = extractMustacheValue(ELSE_MUSTACHE_REGEX, line);

            // 만약 ModelView의 Attribute에 포함된 값이라면 현재 Scope를 넘김
            if (attributes.contains(mustacheValue)) {
                while (isNotEndMustache(mustacheValue, br.readLine())) {}
                return;
            }

            // else문 처리
            handleElse(mustacheValue);
            return;
        }

        // Mustache로만 이뤄진 줄이며 해당 파일을 template으로 사용
        if (line.matches(TEMPLATE_MUSTACHE_REGEX)) {
            String mustacheValue = extractMustacheValue(TEMPLATE_MUSTACHE_REGEX, line);

            handleTemplate(mustacheValue);
            return;
        }

        // Mustache만으로 이뤄진게 아닌 평문과 함께 들어가있는 경우
        convertMustacheOfCurrentLine(line, MustacheCategory.OTHER);
    }

    /**
     * 현재 줄 내에 받은 정규 표현식을 활용하여 Mustache 내부의 값을 빼내주는 메솓
     * @param regex 사용할 정규 표현식
     * @param line 현재 줄
     * @return Mustache 내부에서 빼낸 값
     */
    private static String extractMustacheValue(String regex, String line) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * 마지막을 나타내는 Mustache ({{/XXX}})가 아닌지 확인해주는 메소드
     * @param mustacheValue 현재 Mustache 내부의 값
     * @param line 현재 줄
     * @return 마지막을 나타내는 Mustache 여부
     */
    private static boolean isNotEndMustache(String mustacheValue, String line) {
        Pattern pattern = Pattern.compile(END_MUSTACHE_REGEX);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return !matcher.group(MUSTACHE_GROUP_INDEX).equals(mustacheValue);
        }

        return true;
    }

    /**
     * 현재 가져온 Attribute가 List형일 때, 그 List의 내용을 가져와서 반복문을 돌며 Mustache를 변환해주는 메소드
     * @param mustacheValue Mustache 내부의 값
     * @param attribute 가져온 Attribute 객체
     * @throws IOException BufferedReader에서 파일 내용을 제대로 못읽을 때 발생하는 예외
     */
    private static void handleList(String mustacheValue, List<?> attribute) throws IOException {
        // 빈 리스트일 때는 해당 Scope를 넘긴 후 돌아가야 함
        if (attribute.isEmpty()) {
            while (isNotEndMustache(mustacheValue, br.readLine())) {}
            return;
        }

        List<String> stringsForLoop = new ArrayList<>();
        String line;

        // Scope 안의 모든 line을 가져옴
        while (isNotEndMustache(mustacheValue, line = br.readLine())) {
            stringsForLoop.add(line);
        }

        // 반복문을 돌며 Mustache를 변환
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

    /**
     * 현재 가져온 Attribute를 활용하여 Mustache를 변환해주는 메소드
     * @param mustacheValue Mustache 내부의 값
     * @param attribute 가져온 Attribute 객체
     * @throws IOException BufferedReader에서 파일 내용을 제대로 못읽을 때 발생하는 예외
     */
    private static void handleIf(String mustacheValue, Object attribute) throws IOException {
        String line;

        // Scope 안의 모든 line을 가져옴
        while (isNotEndMustache(mustacheValue, line = br.readLine())) {
            if (hasMustache(line)) {
                convertMustacheOfCurrentLine(line, MustacheCategory.THIS_OR_GETTER, attribute);
                continue;
            }

            insertStringInContentList(line);
        }
    }

    /**
     * 현재 Mustache 내 값에 해당하는 Attribute가 없을 때의 내용을 변환해주는 메소드
     * @param mustacheValue Mustache 내부의 값
     * @throws IOException BufferedReader에서 파일 내용을 제대로 못읽을 때 발생하는 예외
     */
    private static void handleElse(String mustacheValue) throws IOException {
        String line;

        // Scope 안의 모든 line을 가져옴
        while (isNotEndMustache(mustacheValue, line = br.readLine())) {
            if (hasMustache(line)) {
                convertMustacheOfCurrentLine(line, MustacheCategory.OTHER);
                continue;
            }

            insertStringInContentList(line);
        }
    }

    /**
     * 현재 줄의 Mustache를 Mustache의 종류에 맞게 변환해주는 메소드
     * @param currLine 현재 줄
     * @param mustacheCategory Mustache의 종류
     * @param objects Mustache 변환에 필요한 객체들, 가져온 Attribute 또는 List 내 원소 및 index (Attribute가 List의 경우)
     */
    private static void convertMustacheOfCurrentLine(String currLine, MustacheCategory mustacheCategory, Object... objects) {
        // 정규 표현식을 활용하여 현재 줄의 Mustache를 모두 확인
        Pattern pattern = Pattern.compile(EACH_MUSTACHE_REGEX);
        Matcher matcher = pattern.matcher(currLine);

        // 앞 String을 넣기 위한 인덱스
        int fromIdx = 0;

        // 현재 Mustache의 시작, 종료 인덱스
        int mustacheStartIdx, mustacheEndIdx;

        // 현재 Mustache 안의 값
        String mustacheValue;

        // 변환된 내용을 담을 StringBuilder형 객체
        StringBuilder sb = new StringBuilder();

        // Matcher를 사용하여 모든 Mustache를 확인
        while (matcher.find()) {
            // 인덱스 및 Mustache 내 값 초기화
            mustacheStartIdx = matcher.start();
            mustacheEndIdx = matcher.end();
            mustacheValue = matcher.group(MUSTACHE_GROUP_INDEX);

            try {
                // 변환하려는 부분이 List형을 활용할 때
                if (mustacheCategory == MustacheCategory.LIST) {
                    // List에서 가져온 원소와 원소의 인덱스
                    Object element = objects[0];
                    int index = (int) objects[1];

                    convertForList(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue, element, index);
                    continue;
                }

                // this 또는 Getter를 활용하여 변환을 해야할 때
                if (mustacheCategory == MustacheCategory.THIS_OR_GETTER) {
                    // 가져온 Attribute
                    Object attribute = objects[0];

                    convertThisOrGetter(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue, attribute);
                    continue;
                }

                // 그 외의 상황
                convertOther(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue);
            } finally {
                // 앞 String의 시작점 인덱스 이동
                fromIdx = mustacheEndIdx;
            }
        }

        // 뒷 부분이 남아있을 경우 모두 붙여줌
        if (fromIdx != currLine.length()) {
            sb.append(currLine, fromIdx, currLine.length());
        }

        // 완성된 String을 넣음
        insertStringInContentList(sb.toString());
    }

    /**
     * List 내의 원소 값을 가져와 Mustache 부분을 변환해주는 메소드
     * @param sb 변환된 내용을 담을 StringBuilder형 객체
     * @param currLine 현재 줄
     * @param fromIdx 현재 줄에서 확인해볼 부분의 시작 인덱스
     * @param mustacheStartIdx 현재 줄에서의 Mustache 시작 인덱스
     * @param mustacheValue Mustache 내부 값
     * @param element List의 한 원소
     * @param index 원소의 인덱스
     */
    private static void convertForList(StringBuilder sb, String currLine, int fromIdx, int mustacheStartIdx,
                                       String mustacheValue, Object element, int index) {
        // Mustache 안에 '#index'라고 돼있는 경우, loop의 index를 가져와서 넣음
        if ("_index".equals(mustacheValue) && index >= 0) {
            sb.append(currLine, fromIdx, mustacheStartIdx).append(index + 1);
            return;
        }

        // 나머지의 경우, this 또는 Getter를 활용
        convertThisOrGetter(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue, element);
    }

    /**
     * 가져온 Attribute를 활용하여 this를 표현하거나 Getter를 활용하여 Mustache 부분을 변환해주는 메소드
     * @param sb 변환된 내용을 담을 StringBuilder형 객체
     * @param currLine 현재 줄
     * @param fromIdx 현재 줄에서 확인해볼 부분의 시작 인덱스
     * @param mustacheStartIdx 현재 줄에서의 Mustache 시작 인덱스
     * @param mustacheValue Mustache 내부 값
     * @param attribute 가져온 Attribute
     */
    private static void convertThisOrGetter(StringBuilder sb, String currLine, int fromIdx, int mustacheStartIdx,
                                            String mustacheValue, Object attribute) {
        // Mustache 안에 'this'라고 돼있는 경우, 현재 객체를 가져와서 넣음
        if ("this".equals(mustacheValue)) {
            sb.append(currLine, fromIdx, mustacheStartIdx).append(attribute);
            return;
        }

        // Getter 실행
        invokeGetter(sb, currLine, fromIdx, mustacheStartIdx, mustacheValue, attribute);
    }

    /**
     * 가져온 Attribute에서 Mustache 내부 값을 Property로 간주하여 Getter를 실행하여 Mustache 부분을 변환해주는 메소드
     * @param sb 변환된 내용을 담을 StringBuilder형 객체
     * @param currLine 현재 줄
     * @param fromIdx 현재 줄에서 확인해볼 부분의 시작 인덱스
     * @param mustacheStartIdx 현재 줄에서의 Mustache 시작 인덱스
     * @param mustacheValue Mustache 내부 값
     * @param attribute 가져온 Attribute
     */
    private static void invokeGetter(StringBuilder sb, String currLine, int fromIdx, int mustacheStartIdx,
                                     String mustacheValue, Object attribute) {
        // this.XXX 또는 (mustache 값).XXX로 오면 XXX를 property 이름으로 간주
        if (mustacheValue.startsWith("this.") || mustacheValue.startsWith(mustacheValue + ".")) {
            mustacheValue = mustacheValue.split("\\.")[1];
        }

        try {
            // Property를 활용하여 Getter 메소드를 가져온 후 Mustache 값을 그 Getter의 반환 값으로 변환
            Method getter = attribute.getClass().getMethod("get" + mustacheValue.substring(0, 1).toUpperCase(Locale.ROOT) + mustacheValue.substring(1));
            Object returned = getter.invoke(attribute);

            sb.append(currLine, fromIdx, mustacheStartIdx).append(returned.toString());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new PropertyNotFoundException(mustacheValue);
        }
    }

    /**
     * 현재 Mustache 내부의 값을 통해 Attribute를 가져와 변환해주는 메소드
     * @param sb 변환된 내용을 담을 StringBuilder형 객체
     * @param currLine 현재 줄
     * @param fromIdx 현재 줄에서 확인해볼 부분의 시작 인덱스
     * @param mustacheStartIdx 현재 줄에서의 Mustache 시작 인덱스
     * @param mustacheValue Mustache 내부 값
     */
    private static void convertOther(StringBuilder sb, String currLine, int fromIdx, int mustacheStartIdx, String mustacheValue) {
        if (!attributes.contains(mustacheValue)) {
            throw new AttributeNotFoundException(mustacheValue);
        }

        Object attribute = attributes.getAttribute(mustacheValue);

        sb.append(currLine, fromIdx, mustacheStartIdx).append(attribute.toString());
    }

    /**
     * 현재 Mustache가 Template 파일을 불러오는 것일 때 해당 파일을 불러와 변환해주는 메소드
     * @param mustacheValue Mustache 내부 값
     */
    private static void handleTemplate(String mustacheValue) {
        // 기존 BufferedReader와 Byte를 담는 list를 저장
        BufferedReader originBr = br;
        List<Byte> originByteList = byteList;

        // Template HTML 파일을 읽음
        readHtmlFile(CONTEXT_PATH + "/" + mustacheValue + ".html", attributes);

        // BufferedReader를 다시 되돌려놓고 list는 합침
        br = originBr;
        byteList.addAll(0, originByteList);
    }

    /**
     * 현재 줄을 byte형으로 변환하여 Byte형을 담는 list에 넣어주는 메소드
     * @param line 현재 줄
     */
    private static void insertStringInContentList(String line) {
        for (byte currByte : StringUtils.getBytesUtf8(line)) {
            byteList.add(currByte);
        }

        // 개행 추가
        byteList.add(StringUtils.getBytesUtf8("\n")[0]);
    }

    /**
     * Byte형을 담는 list를 byte형을 담는 배열로 변환해주는 메소드
     * @return byte형으로 변환된 내용을 담은 배열
     */
    public static byte[] listToArray() {
        int size = byteList.size();
        byte[] byteArray = new byte[size];

        for (int i = 0; i < size; i++) {
            byteArray[i] = byteList.get(i);
        }

        return byteArray;
    }

    /**
     * Mustache의 종류를 나타내주는 Enum 클래스
     */
    private enum MustacheCategory {
        LIST, THIS_OR_GETTER, OTHER
    }
}
