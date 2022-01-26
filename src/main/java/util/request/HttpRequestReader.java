package util.request;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Map;

public class HttpRequestReader implements AutoCloseable{
    private static final Logger log = LoggerFactory.getLogger(HttpRequestReader.class);

    private BufferedReader br;

    public HttpRequestReader(InputStream in) {
        this.br = new BufferedReader(new InputStreamReader(in));
    }

    public HttpRequest read() throws IOException {
        HttpRequest httpRequest = parsingLine();
        parsingHeader(httpRequest);
        return httpRequest;
    }

    private HttpRequest parsingLine() throws IOException {
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

    private void parsingHeader(HttpRequest httpRequest) throws IOException {
        String line = br.readLine();
        while (!Strings.isNullOrEmpty(line)) {
            line = br.readLine();
            log.debug("request header {}: ", line);
        }
    }

    @Override
    public void close() throws Exception {
        br.close();
    }
}
