package webserver.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class HttpResponseStatusLine {
    private String version;
    private Integer code;
    private String message;

    public void setVersion(String version) {
        this.version = version;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
