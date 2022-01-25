package infrastructure.model;

import java.util.Collections;
import java.util.Set;

public class RequestHeader {

    private final Set<Pair> headers;

    public RequestHeader(Set<Pair> headers) {
        this.headers = headers;
    }

    public Set<Pair> getHeaders() {
        return Collections.unmodifiableSet(headers);
    }
}
