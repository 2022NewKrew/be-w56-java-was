package http;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
public class HttpRequest {

    public static final String SPLIT_REGEX = " ";
    public static final String QUERY_SPLIT_REGEX = "\\?";
    private String httpMethod;
    private String url;
    private String protocol;
    private Map<String, String> parsedQueryString;
    private Map<String, String> parsedBodyString;
    private Map<String, String> parsedHeader = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        parseRequestLine(br);
        parseQueryString(url);
        parseHeader(br);
        if (parsedHeader.containsKey("Content-Length")) {
            parseBody(br, Integer.parseInt(parsedHeader.get("Content-Length")));
        }
    }

    private void parseRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line != null) {
            List<String> headerTokens = new ArrayList<>(Arrays.asList(line.split(SPLIT_REGEX)));
            this.httpMethod = headerTokens.get(0);
            this.url = headerTokens.get(1);
            this.protocol = headerTokens.get(2);
            log.debug("Request Line {} {} {}", httpMethod, url, protocol);
        }
    }

    private void parseHeader(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null && !line.equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            parsedHeader.put(pair.getKey(), pair.getValue());
            log.debug("Header {} {}", pair.getKey(), pair.getValue());
        }
    }

    private void parseQueryString(String url) {
        if (url != null && (url.contains("?"))) {
            List<String> splittedUrl = new ArrayList<>(Arrays.asList(url.split(QUERY_SPLIT_REGEX)));
            this.url = splittedUrl.get(0);
            String queryString = splittedUrl.get(1);
            parsedQueryString = HttpRequestUtils.parseQueryString(queryString);
        }
    }

    private void parseBody(BufferedReader br, int contentLength) throws IOException {
        parsedBodyString = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
    }
}
