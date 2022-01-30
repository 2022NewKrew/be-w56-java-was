package http;

import com.google.common.base.Strings;

public class RequestBody {
    private final Parameters parameters;

    public RequestBody() {
        parameters = new Parameters();
    }

    private RequestBody(Parameters parameters) {
        this.parameters = parameters;
    }

    public static RequestBody create(String string) {
        return new RequestBody(Parameters.create(string));
    }

    public Parameters getQueryParameters() {
        return parameters;
    }
}
