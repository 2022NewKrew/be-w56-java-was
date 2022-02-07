package myspring.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import myspring.users.UserDto;
import webserver.annotations.Component;
import webserver.configures.HandlerInterceptor;
import webserver.configures.HttpServletRequest;
import webserver.configures.HttpServletResponse;
import webserver.context.HttpSession;

import java.util.Optional;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession();
        Optional<UserDto> sessionedUser = Optional.ofNullable((UserDto)session.getAttribute("sessionedUser"));
        if(!sessionedUser.isPresent()) {
            LOGGER.info("Need to Login!");
            response.sendRedirect("/user/login");
            return false;
        }
        request.setAttribute("userSeq", sessionedUser.get().getUserSeq());
        return true;
    }

}
