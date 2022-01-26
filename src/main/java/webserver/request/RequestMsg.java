package webserver.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestMsg {
    RequestStartLine requestStartLine;
    RequestHeaders requestHeaders;
    RequestBody requestBody;
}
