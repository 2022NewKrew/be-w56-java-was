package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import di.annotation.Bean;
import was.http.domain.service.view.View;
import was.http.meta.HttpHeaders;
import was.http.meta.HttpStatus;
import was.http.meta.MediaTypes;

import java.nio.charset.StandardCharsets;

@Bean
public class ObjectViewResolver implements ViewResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public View resolve(Object result) {
        try {
            final byte[] body = objectMapper.writeValueAsString(result)
                    .getBytes(StandardCharsets.UTF_8);

            return new View(HttpStatus.OK, body)
                    .addHeader(HttpHeaders.CONTENT_TYPE, MediaTypes.APPLICATION_JSON.getValue());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("제이슨 직렬화에 실패하였습니다.");
        }
    }
}
