package infrastructure.util;

import application.exception.session.NonExistsSessionException;
import application.exception.session.SessionExpiredException;
import application.in.session.GetSessionUseCase;
import infrastructure.exception.AuthenticationException;
import infrastructure.model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class LoginFilter {

    private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);
    private static final Set<String> FILTERED_URL = Set.of("/user/list");
    private final GetSessionUseCase getSessionUseCase;

    public LoginFilter(GetSessionUseCase getSessionUseCase) {
        this.getSessionUseCase = getSessionUseCase;
    }

    public static boolean matchUrl(String value) {
        return FILTERED_URL.contains(value);
    }

    public void preHandle(HttpRequest httpRequest) throws AuthenticationException {
        String sessionId = HttpRequestUtils.parseCookies(httpRequest.getHeader("Cookie")).get("SESSION_ID");

        try {
            getSessionUseCase.getSession(Long.valueOf(sessionId));
        } catch (NonExistsSessionException e) {
            log.debug(e.getMessage());
            throw new AuthenticationException();
        } catch (SessionExpiredException e) {
            log.debug(e.getMessage());
            throw new AuthenticationException();
        }
    }
}
