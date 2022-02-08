package webserver;

import java.util.HashMap;
import java.util.Map;

import dynamic.html.parser.DefaultParser;
import dynamic.html.parser.UserListParser;

public final class DynamicHtmlMapper {
    private static final String USER_LIST_HTML = "/user/list(.*)";
    private static final String DEFAULT_HTML = "/default/(.*)";
    private static final Map<String, DefaultParser> dynamicParserMap;

    static {
        dynamicParserMap = new HashMap<>();
        dynamicParserMap.put(USER_LIST_HTML, new UserListParser());
        dynamicParserMap.put(DEFAULT_HTML, new DefaultParser());
    }

    private DynamicHtmlMapper() {}

    public static DefaultParser getMapper(String url) {
        return dynamicParserMap.get(
                dynamicParserMap.keySet().stream().filter(url::matches)
                        .findFirst().orElse(DEFAULT_HTML)
        );
    }
}
