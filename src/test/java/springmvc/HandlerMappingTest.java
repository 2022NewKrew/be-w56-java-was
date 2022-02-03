package springmvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springmvc.controller.Controller;
import springmvc.controller.UserController;
import webserver.HttpMethod;
import webserver.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class HandlerMappingTest {

    @Test
    @DisplayName("uri에 해당하는 컨트롤러 반환 테스트")
    void success() {
        // Given
        HttpMethod method = HttpMethod.GET;
        String uri = "/user/create";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();

        HttpRequest httpRequest = new HttpRequest(method, uri, version, headers, "");

        // When
        Optional<Controller> controller = HandlerMapping.getController(httpRequest);

        // Then
        assertThat(controller.get().getClass()).isEqualTo(UserController.class);
    }

    @Test
    @DisplayName("잘못된 uri에 대한 컨트롤러 반환 테스트")
    void fail() {
        // Given
        HttpMethod method = HttpMethod.GET;
        String uri = "/wrong";
        String version = "HTTP/1.1";
        Map<String, String> headers = new HashMap<>();

        HttpRequest httpRequest = new HttpRequest(method, uri, version, headers, "");

        // When
        Optional<Controller> controller = HandlerMapping.getController(httpRequest);

        // Then
        assertThat(controller.isPresent()).isEqualTo(false);
    }


}
