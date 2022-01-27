package router;

import http.Request;
import http.Response;

public class DefaultRouterFunction implements RouterFunction {

    private final RequestPredicate predicate;
    private final RouterFunction f;
    private final router.DefaultRouterFunction next;

    public DefaultRouterFunction(RequestPredicate predicate, RouterFunction f, router.DefaultRouterFunction next) {
        this.predicate = predicate;
        this.f = f;
        this.next = next;
    }

    @Override
    public Response handle(Request request) {
        if (predicate.test(request)) {
            return f.handle(request);
        }
        if (next != null) {
            return next.handle(request);
        }
        return null;
    }

    @Override
    public RouterFunction andRoute(RequestPredicate predicate, RouterFunction f) {
        DefaultRouterFunction next = new DefaultRouterFunction(predicate, f, null);
        return new DefaultRouterFunction(this.predicate, this.f, next);
    }
}
