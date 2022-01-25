package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class RequestReader {
    static final String BLANK_LINE = "";
    private static final Logger log = LoggerFactory.getLogger(RequestReader.class);

    public HttpRequest read(BufferedReader br) throws IOException {
        HttpRequest httpRequest = parsingLine(br);
        parsingHeader(br, httpRequest);
        return httpRequest;
    }

    private HttpRequest parsingLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.info("request line {}: ", line);

        String[] requestLine = line.split(" ");
        String[] urlSplit = requestLine[1].split("\\?");

        return HttpRequest.builder()
                .method(MethodType.of(requestLine[0]))
                .url(urlSplit[0])
                .httpVersion(HttpVersion.of(requestLine[2]))
                .queryParams(parsingQueryParams(urlSplit))
                .build();
    }

    private Map<String, String> parsingQueryParams(String[] urlSplit){
        if(urlSplit.length == 1){
            return Collections.emptyMap();
        }
        return HttpRequestUtils.parseQueryString(urlSplit[1]);
    }

    private void parsingHeader(BufferedReader br, HttpRequest httpRequest) throws IOException {
        String line = br.readLine();
        while (!line.equals(BLANK_LINE)) {
            line = br.readLine();
            log.debug("request header {}: ", line);
        }
    }
}
