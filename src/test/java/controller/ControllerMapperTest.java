package controller;

import static org.assertj.core.api.Assertions.assertThat;

import http.HttpHeader;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;
import http.RequestParameters;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils;

public class ControllerMapperTest {

    private ControllerMapper controllerMapper;

    @BeforeEach
    public void init() {
        controllerMapper = ControllerMapper.create();
    }

    @Test
    @DisplayName("Mapping to Auth Controller - 로그인 테스트")
    public void loginMappingTest() {
        // Given
        RequestParameters bodyParameters = RequestParameters.of(
            Map.of("userId", "id", "password", "pwd"));
        HttpRequest request = HttpRequest.builder()
            .method(HttpMethod.POST)
            .path("/user/login")
            .bodyParameters(bodyParameters)
            .build();

        // When
        HttpResponse response = controllerMapper.handleRequest(request);

        // Then
        assertThat(response.respondStatus())
            .isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getHeader("Set-Cookie"))
            .isNotBlank();
        assertThat(HttpRequestUtils.parseCookies(response.getHeader("Set-Cookie")).get("logined"))
            .isNotBlank();
        assertThat(response.getHeader("Location"))
            .isNotBlank();
    }

    @Test
    @DisplayName("Mapping to User Controller - 사용자 목록 조회 테스트")
    public void readUserMappingTest() {
        // Given
        HttpHeader header = HttpHeader.of(Map.of("Cookie", "logined=false"));
        HttpRequest request = HttpRequest.builder()
            .method(HttpMethod.GET)
            .path("/user/list")
            .header(header)
            .build();

        // When
        HttpResponse response = controllerMapper.handleRequest(request);

        // Then
        assertThat(response.respondStatus())
            .isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getHeader("Location"))
            .isEqualTo("/user/login.html");
    }

    @Test
    @DisplayName("Mapping to User Controller - 회원가입 테스트")
    public void createUserMappingTest() {
        // Given
        RequestParameters bodyParameters = RequestParameters.of(
            Map.of("userId", "id", "password", "pwd", "name", "name", "email", "email@test.com"));
        HttpRequest request = HttpRequest.builder()
            .method(HttpMethod.POST)
            .path("/user/create")
            .bodyParameters(bodyParameters)
            .build();

        // When
        HttpResponse response = controllerMapper.handleRequest(request);

        // Then
        assertThat(response.respondStatus())
            .isEqualTo(HttpStatus.FOUND.value());
        assertThat(response.getHeader("Location"))
            .isEqualTo("/index.html");
    }
}
