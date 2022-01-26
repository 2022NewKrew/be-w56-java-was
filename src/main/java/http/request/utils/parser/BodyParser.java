package http.request.utils.parser;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BodyParser extends AbstractKeyValueParser {
    private static final String BODY_ITEM_DELIMITER = "&";
    private static final String BODY_KEY_VALUE_DELIMITER = "=";

    public static Map<String, String> parseBody(String body) {
        String decodedBody = URLDecoder.decode(body, StandardCharsets.UTF_8);
        return parseInternal(decodedBody, BODY_ITEM_DELIMITER, BODY_KEY_VALUE_DELIMITER);
    }
}
