package webserver.request;

import lombok.*;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class RequestInfo{
    @NonNull
    private String method;
    @NonNull
    private String protocol;
    @NonNull
    private String url;
    private String queryString;
}