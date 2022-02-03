package http;

import util.Pair;
import util.ParsingUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
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

    public static Parameters create(Optional<String> parameters) {
        if (parameters.isEmpty()) {
            return new Parameters();
        }
        return Parameters.create(parameters.get());
    }

    public static Parameters create(String parameters) {
        String[] tokens = URLDecoder.decode(parameters, StandardCharsets.UTF_8).split(DELIMITER);
        Map<String, String> result = Arrays.stream(tokens)
                .map(token -> ParsingUtils.getKeyValue(token, "="))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        return new Parameters(result);
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
