package http;

import com.google.common.base.Strings;
import util.ParsingUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Parameters {
    static final String PARAMETER_DELIMITER = "&";
    static final String KEY_VALUE_DELIMITER = "=";

    Map<String, String> parameters;

    private Parameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public static Parameters create(String parameters) {
        if (Strings.isNullOrEmpty(parameters)) {
            return new Parameters(new HashMap<>());
        }

        String decode = URLDecoder.decode(parameters, StandardCharsets.UTF_8);
        Map<String, String> result = ParsingUtils.parse(decode, PARAMETER_DELIMITER, KEY_VALUE_DELIMITER);
        return new Parameters(result);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
