package myspring.configures.web;

import myspring.reply.ReplyRequest;
import myspring.users.UserDto;
import webserver.configures.*;
import webserver.context.HttpSession;

public class ReplyRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ReplyRequestResolver.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession();

        UserDto user = (UserDto)session.getAttribute("sessionedUser");
        long userSeq = user.getUserSeq();
        long articleSeq = Long.parseLong(webRequest.getParameter("articleSeq"));
        String writer = webRequest.getParameter("writer");
        String title = webRequest.getParameter("title");
        String contents = webRequest.getParameter("contents");

        return new ReplyRequest(userSeq, articleSeq, writer, title, contents);
    }
}
