package util;

import com.google.common.base.Strings;
import http.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestParser {

    private HttpRequestParser() {
    }

    public static RequestLine parseStartLine(String line) {
        String[] tokens = parse(line, RequestLine.DELIMITER, RequestLine.PARAMETER_COUNT);
        return RequestLine.create(tokens);
    }

    public static Headers parseHeaders(List<String> headers) {
        Map<FieldName, FieldValue> map = headers.stream()
                .map(HttpRequestParser::parseHeader)
                .collect(Collectors.toMap(Header::getFieldName, Header::getFieldValue));
        return new Headers(map);
    }

    private static Header parseHeader(String header) {
        String[] tokens = parse(header, Header.DELIMITER, Header.PARAMETER_COUNT);
        return Header.create(tokens);
    }

    private static String[] parse(String string, String delimiter, int count) {
        validateNull(string);
        String[] tokens = string.split(delimiter);
        if (tokens.length != count) {
            throw new IllegalArgumentException();
        }
        return tokens;
    }

    private static void validateNull(String string) {
        if (Strings.isNullOrEmpty(string)) {
            throw new IllegalArgumentException();
        }
    }
}
