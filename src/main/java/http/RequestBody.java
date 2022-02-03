package http;

import java.util.Optional;

public class RequestBody {
    private final Parameters parameters;

    private RequestBody(Parameters parameters) {
        this.parameters = parameters;
    }

    public static RequestBody create(Optional<String> string) {
        return new RequestBody(Parameters.create(string));
    }

    public Parameters getQueryParameters() {
        return parameters;
    }
}
