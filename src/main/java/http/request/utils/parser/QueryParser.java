package http.request.utils.parser;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class QueryParser extends AbstractKeyValueParser {
    private static final String QUERY_ITEM_DELIMITER = "&";
    private static final String QUERY_KEY_VALUE_DELIMITER = "=";

    public static Map<String, String> parseQuery(String query) {
        String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
        return parseInternal(decodedQuery, QUERY_ITEM_DELIMITER, QUERY_KEY_VALUE_DELIMITER);
    }
}
