package webserver.http;

import controller.Controller;
import controller.IndexController;
import http.HandlerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HandlerMapperTest {

    HandlerMapper handlerMapper = HandlerMapper.getInstance();

    @DisplayName("컨트롤러 리턴 여부 확인")
    @Test
    void get() {
        Controller controller = handlerMapper.get("GET", "/");
        assertThat(controller.getClass()).isEqualTo(IndexController.class);
    }

    @DisplayName("일치하는 컨트롤러가 없는 경우")
    @Test
    void getNull(){
        Controller controller = handlerMapper.get("GET", "/favicon.ico");
        assertThat(controller).isEqualTo(null);
    }
}