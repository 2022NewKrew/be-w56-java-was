package util.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HttpResponse<T> {
    private HttpResponseStatus status;
    private T data;
    private HttpResponseDataType dataType;
}
