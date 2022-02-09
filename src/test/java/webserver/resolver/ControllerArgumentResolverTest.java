package webserver.resolver;

import static org.assertj.core.api.Assertions.assertThat;

import dto.UserSignupRequest;
import http.HttpRequest;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerArgumentResolverTest {

    private final HandlerMethodArgumentResolver resolver = new ControllerArgumentResolver();
    private final Map<String, String> parameters = Map.of(
        "userId", "jin.jang",
        "password", "12345678",
        "name", "장우진",
        "email", "jin.jang@kakaocorp.com");
    private final HttpRequest httpRequest = HttpRequest.builder()
        .parameters(parameters)
        .build();

    @Test
    @DisplayName("파라미터를 지원하는지 확인한다.")
    void supportsParameter() {
        assertThat(resolver.supportsParameter(UserSignupRequest.class, httpRequest)).isTrue();
    }

    @Test
    @DisplayName("생성자를 통해 인스턴스를 생성한다.")
    void resolveArgument() throws Exception {
        UserSignupRequest request = (UserSignupRequest) resolver.resolveArgument(
            new UserSignupRequest(), httpRequest);

        assertThat(request.getUserId()).isEqualTo(parameters.get("userId"));
        assertThat(request.getPassword()).isEqualTo(parameters.get("password"));
        assertThat(request.getName()).isEqualTo(parameters.get("name"));
        assertThat(request.getEmail()).isEqualTo(parameters.get("email"));
    }
}