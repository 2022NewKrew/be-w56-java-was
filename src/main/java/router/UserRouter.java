package router;

import handler.UserHandler;

import static router.RequestPredicate.post;

public class UserRouter {

    public RouterFunction route(UserHandler handler) {
        return RouterFunction
                .route(post("/user/login"), handler::login)
                .andRoute(post("/user/create"), handler::create);
    }
}
