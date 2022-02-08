package infrastructure.staticservice;

import http.common.Status;
import http.request.HttpRequest;
import infrastructure.dto.AppResponse;

public class StaticController {

    public AppResponse getStatic(HttpRequest httpRequest) {
        return AppResponse.of(httpRequest.getPath(), Status.OK);
    }

    public AppResponse err405(HttpRequest httpRequest) {
        return AppResponse.of("", Status.NOT_ALLOWED);
    }
}
