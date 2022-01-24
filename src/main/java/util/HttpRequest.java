package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.HttpRequestUtils.parseHeader;
import static util.HttpRequestUtils.parseQueryString;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private final HttpMethod method;
    private final URL url;
    private final String httpVersion;
    private final Map<String, String> headers;
    private final Map<String, String> body;

    public HttpRequest(String line) {
        String[] words = line.split(" ");
        this.method = HttpMethod.valueOf(words[0]);
        this.url = new URL(words[1]);
        this.httpVersion = words[2];
        this.headers = null;
        this.body = null;
    }

    public HttpRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] words = line.split(" ");
        this.method = HttpMethod.valueOf(words[0]);
        this.url = new URL(words[1]);
        this.httpVersion = words[2];

        //header parsing
        List<HttpRequestUtils.Pair> pairs = new ArrayList<>();
        while (!line.equals("")) {
            line = br.readLine();
            pairs.add(parseHeader(line));
            log.debug("header : {}", line);
        }

        this.headers = pairs.stream().filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        Map<String, String> body = null;

        if(this.method == HttpMethod.POST) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            String queryString = IOUtils.readData(br, contentLength);
            body = parseQueryString(queryString);
        }
        this.body = body;
    }


    public HttpMethod method() {
        return this.method;
    }

    public String url() {
        return url.url();
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> params() {
        return url.params();
    }

    public Map<String, String> body(){
        return this.body;
    }
}
