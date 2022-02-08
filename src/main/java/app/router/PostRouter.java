package app.router;

import lib.was.router.RouterFunction;
import lib.was.di.Bean;
import app.handler.PostHandler;

import static lib.was.router.RequestPredicate.get;
import static lib.was.router.RequestPredicate.post;

@Bean
public class PostRouter {

    @Bean
    public RouterFunction route(PostHandler handler) {
        return RouterFunction
                .route(post("/posts"), handler::create)
                .andRoute(get("/"), handler::list);
    }
}
