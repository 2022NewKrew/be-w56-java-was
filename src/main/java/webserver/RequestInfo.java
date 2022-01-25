package webserver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RequestInfo {
    private String url;
    private String method;
    private boolean isStaticResource = false;
}
