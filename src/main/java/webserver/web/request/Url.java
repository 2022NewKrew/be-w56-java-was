package webserver.web.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;

import java.util.Map;

@Getter
@Slf4j
public class Url {

    private String url;
    private Map<String, String> parameters;

    private Url(String url) {
        if(url.contains("?")) {
            parameters = HttpRequestUtils.parseQueryString(url.split("\\?")[1]);
        }
        this.url = url.split("\\?")[0];
        log.debug("url : {}", this.url);
    }

    public static Url getUrl(String startLine) {
        String url = startLine.split(" ")[1];
        return new Url(url);
    }

    @Override
    public String toString() {
        return url;
    }
}
