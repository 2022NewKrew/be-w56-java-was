package router;

import annotation.Bean;
import handler.PostHandler;

import static router.RequestPredicate.get;
import static router.RequestPredicate.post;

@Bean
public class PostRouter {

    @Bean
    public RouterFunction route(PostHandler handler) {
        return RouterFunction
                .route(post("/posts"), handler::create)
                .andRoute(get("/"), handler::list);
    }
}
