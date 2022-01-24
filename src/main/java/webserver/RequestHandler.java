package webserver;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.utils.CommaSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Getter
public class RequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

    private String method;
    private String url;
    private String httpVersion;
    private final Map<String, Object> headers = new HashMap<>();

    public RequestHandler(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        splitRequestLine(br.readLine());

        String line;

        while (!(line = br.readLine()).equals("")) {
            String[] splited = line.split(": ");
            String key = splited[0];
            String value = splited[1];

            if ("Accept".equals(key)
            || "Accept-Language".equals(key)
            || "Accept-Encoding".equals(key)) {
                headers.put(key, new CommaSeries(value));
                continue;
            }

            if ("Content-Length".equals(key)) {
                headers.put(key, Long.parseLong(value));
                continue;
            }

            headers.put(key, value);
        }
    }

    private void splitRequestLine(String requestLine) {
        String[] splited = requestLine.split(" ");

        method = splited[0];
        url = splited[1];

        if ("/".equals(url)) {
            url = "/index.html";
        }

        if (url.charAt(0) != '/') {
            StringBuilder sb = new StringBuilder("/");
            url = sb.append(url).toString();
        }

        httpVersion = splited[2];

        LOGGER.debug("Method: {}, URL: {}", method, url);
    }
}
