package app.router;

import lib.was.router.RouterFunction;
import lib.was.di.Bean;
import app.handler.UserHandler;

import static lib.was.router.RequestPredicate.get;
import static lib.was.router.RequestPredicate.post;

@Bean
public class UserRouter {

    @Bean
    public RouterFunction route(UserHandler handler) {
        return RouterFunction
                .route(post("/user/login"), handler::login)
                .andRoute(post("/user/create"), handler::create)
                .andRoute(get("/user/list"), handler::list);
    }
}
