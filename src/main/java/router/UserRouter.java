package router;

import annotation.Bean;
import handler.UserHandler;

import static router.RequestPredicate.get;
import static router.RequestPredicate.post;

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
