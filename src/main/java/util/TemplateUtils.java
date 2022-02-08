package util;

import exception.ResolveTemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;
import webserver.model.Model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Slf4j
public class TemplateUtils {
    private static final String REGEX_OF_BLOCK_FOR_SPLIT = "(?=\\{\\{#.*\\}\\})|(?=\\{\\{/.*\\}\\})";
    private static final String REGEX_OF_BLOCK_START = "^(\\{\\{#).*(\\}\\})(.|\n)*";
    private static final String REGEX_OF_BLOCK_END = "^(\\{\\{/).*(\\}\\})(.|\n)*";

    private static final String START = "{{";
    private static final String END = "}}";
    private static final String START_OF_BLOCK_START = START + "#";
    private static final String START_OF_BLOCK_END = START + "/";

    private static final String PREFIX_OF_GETTER = "get";


    public static byte[] parse(File file, Model model) throws IOException {
        String original = Files.readString(file.toPath());
        String blockReplaced = resolveBlock(original, model);
        return blockReplaced.getBytes();
    }

    private static String resolveBlock(String line, Model model) {
        String[] splits = line.split(REGEX_OF_BLOCK_FOR_SPLIT);
        Stack<String> checker = new Stack<>();
        StringBuilder replaced = new StringBuilder();

        for (String split : splits) {
            if (isStartOfBlock(split)) {
                String nameOfList = StringUtils.substring(split, split.indexOf(START_OF_BLOCK_START) + START_OF_BLOCK_START.length(), split.indexOf(END));
                String forEach = split.substring((START_OF_BLOCK_START + nameOfList + END).length());
                replaced.append(doForEach(forEach, model.getList(nameOfList)));
                checker.push(nameOfList);
                continue;
            }

            if (isEndOfBlock(split)) {
                String keyword = StringUtils.substring(split, split.indexOf(START_OF_BLOCK_END) + START_OF_BLOCK_START.length(), split.indexOf(END));
                if (!StringUtils.equals(keyword, checker.peek())) {
                    throw new ResolveTemplateException();
                }
                replaced.append(split.substring((START_OF_BLOCK_END + keyword + END).length()));
                checker.pop();
                continue;
            }

            replaced.append(split);
        }

        if (!checker.isEmpty()) {
            throw new ResolveTemplateException();
        }
        return replaced.toString();
    }


    private static boolean isStartOfBlock(String line) {
        return line.matches(REGEX_OF_BLOCK_START);
    }

    private static boolean isEndOfBlock(String line) {
        return line.matches(REGEX_OF_BLOCK_END);
    }

    private static String doForEach(String forEach, List<Object> list) {
        if (list.isEmpty()) {
            return "";
        }

        Set<String> words = Set.of(StringUtils.substringsBetween(forEach, START, END));
        Set<Method> methods = getGetterMethods(words, list.get(0).getClass());

        StringBuilder result = new StringBuilder();
        for (Object obj : list) {
            result.append(forEach);
            methods.forEach(method -> replaceByObjectMethod(obj, method, result));
        }
        return result.toString();
    }

    private static Set<Method> getGetterMethods(Collection<String> words, Class<?> type) {
        return ReflectionUtils.getAllMethods(
                        type,
                        ReflectionUtils.withModifier(Modifier.PUBLIC),
                        ReflectionUtils.withPrefix(PREFIX_OF_GETTER)
                ).stream()
                .filter(method -> words.contains(removePrefixOfGetter(method)))
                .collect(Collectors.toSet());
    }

    private static String removePrefixOfGetter(Method method) {
        String name = method.getName().substring(PREFIX_OF_GETTER.length());
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    private static void replaceByObjectMethod(Object obj, Method method, StringBuilder result) {
        try {
            String value = (String) method.invoke(obj);
            String replacedWord = START + removePrefixOfGetter(method) + END;
            int index = result.indexOf(replacedWord);
            result.replace(index, index + replacedWord.length(), value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ResolveTemplateException("메서드 호출에 실패하였습니다. " + method.getName());
        }
    }
}
