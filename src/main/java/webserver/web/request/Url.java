package webserver.web.request;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;

import java.util.Map;

@Getter
@Slf4j
public class Url {

    private String url;
    private Map<String, String> parameters = Maps.newHashMap();

    private Url(String url) {
        this.url = url;
        if (this.url.equals("/")) {
            this.url = "/";
            parameters.put("filePath", "redirect:/board/list");
        }
        if (this.url.contains("?")) {
            parameters = HttpRequestUtils.parseQueryString(url.split("\\?")[1]);
            this.url = url.split("\\?")[0];
        }
        if (this.url.contains(".")) {
            this.url = "/";
            parameters = Maps.newHashMap();
            parameters.put("filePath", url.substring(1));
        }
    }

    public static Url getUrl(String startLine) {
        String url = startLine.split(" ")[1];
        return new Url(url);
    }

    public String getParameterData(String target) {
        return parameters.get(target);
    }

    @Override
    public String toString() {
        return url;
    }
}
