package springmvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.HttpResponse;
import webserver.HttpStatus;

import static org.assertj.core.api.Assertions.*;

class ViewResolverTest {

    @Test
    @DisplayName("302 응답 테스트")
    void test302() {
        // Given
        HttpResponse httpResponse = new HttpResponse();
        String viewName = "redirect:/index.html";

        // When
        ViewResolver.resolve(viewName, httpResponse);

        // Then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
        assertThat(httpResponse.getHeaders().get("Location")).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("정적리소스 반환에 대한 200 응답 테스트")
    void test200() {
        // Given
        HttpResponse httpResponse = new HttpResponse();
        String viewName = "/index.html";

        // When
        ViewResolver.resolve(viewName, httpResponse);

        // Then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("잘못된 uri에 대한 404 응답 테스트")
    void test404() {
        // Given
        HttpResponse httpResponse = new HttpResponse();
        String viewName = "/wrong";

        // When
        ViewResolver.resolve(viewName, httpResponse);

        // Then
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
