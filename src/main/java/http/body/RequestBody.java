package http.body;

import http.Parameters;

import java.util.Optional;

public class RequestBody {
    private final Parameters parameters;

    private RequestBody(Parameters parameters) {
        this.parameters = parameters;
    }

    public static RequestBody create(Optional<String> string) {
        if (string.isPresent()) {
            return new RequestBody(Parameters.create(string.get()));
        }
        return new RequestBody(Parameters.create(null));
    }

    public Parameters getQueryParameters() {
        return parameters;
    }
}
