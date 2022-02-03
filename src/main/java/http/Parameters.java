package http;

import util.Pair;
import util.ParsingUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Parameters {
    static final String PARAMETER_DELIMITER = "&";
    static final String KEY_VALUE_DELIMITER = "=";

    Map<String, String> parameters;

    private Parameters() {
        this.parameters = new HashMap<>();
    }

    private Parameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public static Parameters create(Optional<String> parameters) {
        if (parameters.isEmpty()) {
            return new Parameters();
        }

        String[] tokens = URLDecoder.decode(parameters.get(), StandardCharsets.UTF_8).split(PARAMETER_DELIMITER);
        Map<String, String> result = Arrays.stream(tokens)
                .map(token -> ParsingUtils.getKeyValue(token, KEY_VALUE_DELIMITER))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        return new Parameters(result);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
