package webserver.web.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Method {

    private final String method;

    private Method(String method) {
        this.method = method;
    }

    public static Method getMethod(String startLine) {
        String[] split = startLine.split(" ");
        log.debug("method : {}", split[0]);
        return new Method(split[0]);
    }

    @Override
    public String toString() {
        return method;
    }
}
