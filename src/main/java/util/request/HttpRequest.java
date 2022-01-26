package util.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class HttpRequest {
    private final MethodType method;
    private final String url;
    private final HttpVersion httpVersion;
    private final Map<String, String> queryParams;

    public String getQueryParam(String queryParamName){
        return queryParams.get(queryParamName);
    }
}
