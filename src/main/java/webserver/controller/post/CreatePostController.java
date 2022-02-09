package webserver.controller.post;

import lombok.RequiredArgsConstructor;
import util.converter.ConverterService;
import util.request.HttpRequest;
import util.request.MethodType;
import util.response.ContentType;
import util.response.HttpResponse;
import util.response.HttpStatus;
import webserver.controller.Controller;
import webserver.domain.entity.Post;
import webserver.domain.repository.post.PostRepository;

@RequiredArgsConstructor
public class CreatePostController implements Controller {
    private final PostRepository postRepository;

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return httpRequest.getUrl().startsWith("/posts")
                && httpRequest.getMethod().equals(MethodType.POST);
    }

    @Override
    public HttpResponse doHandle(HttpRequest httpRequest) {
        postRepository.save(createPost(httpRequest));

        return HttpResponse.builder(HttpStatus.REDIRECT, ContentType.HTML)
                .header("Location", "/")
                .build();
    }

    private Post createPost(HttpRequest httpRequest){
        return ConverterService.convert(httpRequest.getBodyParams(), Post.class);
    }
}
