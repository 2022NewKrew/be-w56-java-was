package webserver.request;

import lombok.*;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestInfo{
    @NonNull
    private String method;
    @NonNull
    private String url;
    @NonNull
    private String protocol;
    private String queryString;
}