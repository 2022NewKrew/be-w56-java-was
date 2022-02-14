package webserver.http.request.body;

import lombok.Getter;

@Getter
public class RequestBody {

    private final BodyMap bodyMap;

    public RequestBody(String inputBody) {
        bodyMap = new BodyMap(inputBody);
    }
}
