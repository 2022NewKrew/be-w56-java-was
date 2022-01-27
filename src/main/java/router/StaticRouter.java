package router;

import annotation.Bean;
import handler.StaticHandler;

import static router.RequestPredicate.get;

@Bean
public class StaticRouter {

    @Bean
    public RouterFunction route(StaticHandler handler) {
        return RouterFunction.route(get(".+"), handler::get);
    }
}
