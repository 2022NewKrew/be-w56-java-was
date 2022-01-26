package router;

import handler.StaticHandler;

import static router.RequestPredicate.get;

public class StaticRouter {

    public RouterFunction route(StaticHandler handler) {
        return RouterFunction.route(get(".+"), handler::get);
    }
}
