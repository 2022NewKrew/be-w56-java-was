package controller.adapter;

import http.request.HttpRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class StaticHandlerAdapterTest {

    @Test
    @DisplayName("static file supports 정상 동작 확인")
    public void staticSupports() {
        // given
        StaticHandlerAdapter staticHandlerAdapter = new StaticHandlerAdapter();
        HttpRequest request = new HttpRequest("GET /index.html HTTP/1.1", new ArrayList<>(), new ArrayList<>());

        // when
        boolean supports = staticHandlerAdapter.supports(request);

        // then
        assertThat(supports).isEqualTo(true);
    }
}
