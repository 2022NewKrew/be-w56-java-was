package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import di.annotation.Bean;
import was.http.domain.service.view.ResponseEntity;
import was.http.domain.service.view.View;
import was.http.meta.HttpHeaders;
import was.http.meta.HttpStatus;
import was.http.meta.MediaTypes;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Bean
public class ResponseEntityViewResolver implements ViewResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public View resolve(Object result) {
        if (result instanceof ResponseEntity<?>) {
            try {
                final ResponseEntity<?> responseEntity = ((ResponseEntity<?>) result);

                final HttpStatus httpStatus = responseEntity.getHttpStatus();
                final byte[] body = objectMapper.writeValueAsString(responseEntity.getBody()).getBytes(StandardCharsets.UTF_8);

                final Map<String, String> headers = responseEntity.getHeader();
                headers.put(HttpHeaders.CONTENT_TYPE, MediaTypes.APPLICATION_JSON.getValue());

                return new View(httpStatus, body)
                        .addAllHeader(headers);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("제이슨 직렬화에 실패하였습니다.");
    }
}
