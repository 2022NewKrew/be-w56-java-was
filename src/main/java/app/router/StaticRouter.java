package app.router;

import lib.was.router.RouterFunction;
import lib.was.di.Bean;
import app.handler.StaticHandler;

import static lib.was.router.RequestPredicate.get;

@Bean
public class StaticRouter {

    @Bean
    public RouterFunction route(StaticHandler handler) {
        return RouterFunction.route(get(".+"), handler::get);
    }
}
