package http;

import com.google.common.base.Strings;
import util.HttpRequestParser;
import util.Pair;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Parameters {
    static final String DELIMITER = "&";

    Map<String, String> parameters;

    public Parameters() {
        this.parameters = new HashMap<>();
    }

    public Parameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public static Parameters create(String parameters) {
        if (Strings.isNullOrEmpty(parameters)) {
            return new Parameters();
        }

        String[] tokens = URLDecoder.decode(parameters, StandardCharsets.UTF_8).split(DELIMITER);
        Map<String, String> result = Arrays.stream(tokens)
                .map(token -> HttpRequestParser.getKeyValue(token, "="))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        return new Parameters(result);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
