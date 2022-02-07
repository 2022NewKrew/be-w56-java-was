package myspring.configures.web;

import myspring.article.ArticleRequest;
import myspring.users.UserDto;
import webserver.configures.*;
import webserver.context.HttpSession;

public class ArticleRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ArticleRequestResolver.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();

        UserDto user = (UserDto)session.getAttribute("sessionedUser");
        long userSeq = user.getUserSeq();
        String writer = webRequest.getParameter("writer");
        String title = webRequest.getParameter("title");
        String contents = webRequest.getParameter("contents");

        return new ArticleRequest(userSeq, writer, title, contents);
    }
}
