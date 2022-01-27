package util.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Builder
@Getter
public class HttpResponse<T> {
    private HttpResponseStatus status;

    @Builder.Default
    private Map<String, String> headers = Collections.emptyMap();

    private T data;

    private HttpResponseDataType dataType;

    public static HttpResponse<String> of(Exception e){
        return HttpResponse.<String>builder()
                .status(HttpResponseStatus.INTERNAL_ERROR)
                .data(e.getMessage())
                .dataType(HttpResponseDataType.STRING)
                .build();
    }
}
