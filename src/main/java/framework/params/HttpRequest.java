package framework.params;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class HttpRequest {
    private String url;
    private String method;
    private Cookie cookie;
    private Map<String, String> requestParam;
    private Map<String, String> requestBody;
}
