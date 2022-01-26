package util.request;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestReader implements AutoCloseable{
    private static final Logger log = LoggerFactory.getLogger(HttpRequestReader.class);

    private BufferedReader br;

    public HttpRequestReader(InputStream in) {
        this.br = new BufferedReader(new InputStreamReader(in));
    }

    public HttpRequest read() throws IOException {
        String[] requestLine = parsingLine();
        MethodType methodType = MethodType.of(requestLine[0]);
        String url = parsingUrl(requestLine[1]);
        Map<String, String> queryParams = parsingQueryParams(requestLine[1]);
        HttpVersion httpVersion = HttpVersion.of(requestLine[2]);
        Map<String, String> headers = parsingHeader();

        String body = null;
        if(headers.containsKey("Content-Length")){
            body = parsingBody(Integer.parseInt(headers.get("Content-Length")));
        }

        return HttpRequest.builder()
                .method(methodType)
                .url(url)
                .queryParams(queryParams)
                .httpVersion(httpVersion)
                .headers(headers)
                .body(body)
                .build();
    }

    private String[] parsingLine() throws IOException {
        String line = br.readLine();
        log.info("request line {}: ", line);
        return line.split(" ");
    }

    private String parsingUrl(String urlWithQueryParam){
        return urlWithQueryParam.split("\\?")[0];
    }

    private Map<String, String> parsingQueryParams(String urlWithQueryParam){
        String[] urlSplit = urlWithQueryParam.split("\\?");

        if(urlSplit.length == 1){
            return Collections.emptyMap();
        }
        return HttpRequestUtils.parseQueryString(urlSplit[1]);
    }

    private Map<String, String> parsingHeader() throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line = br.readLine();
        while (!Strings.isNullOrEmpty(line)) {
            String[] split = line.split(": ");
            headers.put(split[0], split[1]);
            log.debug("request header {} : {}", split[0], split[1]);
            line = br.readLine();
        }

        return headers;
    }

    private String parsingBody(int contentLength) throws IOException{
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.valueOf(body);
    }

    @Override
    public void close() throws Exception {
        br.close();
    }
}
