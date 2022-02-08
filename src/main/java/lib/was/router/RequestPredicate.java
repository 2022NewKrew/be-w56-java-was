package lib.was.router;

import lib.was.http.Method;
import lib.was.http.Request;
import lib.was.http.Route;

public class RequestPredicate {

    private final Route route;

    public RequestPredicate(Route route) {
        this.route = route;
    }

    public static RequestPredicate get(String pattern) {
        return new RequestPredicate(new Route(Method.GET, pattern));
    }

    public static RequestPredicate post(String pattern) {
        return new RequestPredicate(new Route(Method.POST, pattern));
    }

    public boolean test(Request request) {
        return route.matches(request);
    }
}
