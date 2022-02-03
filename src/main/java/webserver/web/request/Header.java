package webserver.web.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public class Header {

    private final Map<String, String> headers = new HashMap<>();

    public Header(List<String> request) {
        request.forEach(req -> {
            req = req.replaceFirst(": ", "|");
            String[] split = req.split("\\|");
            if (split.length != 1)
                headers.put(split[0], split[1]);
        });
    }

    public String findHeaderData(String key) {
        String data = headers.get(key);
        if (data == null)
            return "";
        return data;
    }
}
