package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HttpRequestUtils {
    private final static String TEMPLATE_PATTERN = "\\{\\{.*}}";

    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static Map<String, String> parseRequestLine(String requestLine) {
        Map<String, String> ret = new HashMap<>();
        System.out.println(requestLine);
        String[] parsed = requestLine.split(" ");
        String[] urlParsed = parsed[1].split("\\?");
        if (urlParsed.length == 2) {
            ret.put("query", urlParsed[1]);
        }
        ret.put("method", parsed[0]);
        ret.put("url", urlParsed[0]);
        ret.put("protocol", parsed[2]);

        return ret;
    }

    public static Map<String, String> parseResponseHeader(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        do {
            line = br.readLine();
            Pair header = parseHeader(line);
            if (header != null)
                headers.put(header.getKey(), header.getValue());
        } while (!line.equals(""));

        return headers;
    }

    public static byte[] getBodyData(String requestURL) throws IOException {

        File file = new File("./webapp" + requestURL);
        if (file.exists()) {
            String fileString = Files.readString(file.toPath());
            String fileName = file.getName();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

            if (extension.equals("html")) {
                Matcher m = Pattern.compile(TEMPLATE_PATTERN).matcher(fileString);
                while (m.find()) {
                    String convertedString = convert2Template(m.group());
                    fileString = fileString.replaceFirst(TEMPLATE_PATTERN, convertedString);
                }
            }

            return fileString.getBytes(StandardCharsets.UTF_8);
        }

        return Files.readAllBytes(new File("./webapp/404.html").toPath());
    }

    public static String getStatusCode(String requestURL) {
        File file = new File("./webapp" + requestURL);
        if (file.exists()) {
            return "200";
        }

        return "404";
    }

    private static String convert2Template(String template) throws IOException {
        String innerStr = template.substring(2, template.length() - 2).trim();

        System.out.println("안의 문자 : " + innerStr);

        if (innerStr.startsWith(">")) {
            String fileName = innerStr.substring(1);
            System.out.println("파일 이름 : " + fileName);
            File file = new File("./webapp/" + fileName);
            System.out.println("파일 생성");
            String fileString = Files.readString(file.toPath());
            System.out.println(fileString);
            return fileString;
        }

        return "";
    }
}


