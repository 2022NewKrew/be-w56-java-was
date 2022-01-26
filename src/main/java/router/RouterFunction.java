package router;

import http.Request;
import http.Response;

@FunctionalInterface
public interface RouterFunction {

    Response handle(Request request);

    default RouterFunction andRoute(RequestPredicate predicate, RouterFunction f) {
        return request -> null;
    }

    static RouterFunction route(RequestPredicate predicate, RouterFunction f) {
        return new DefaultRouterFunction(predicate, f, null);
    }
}
