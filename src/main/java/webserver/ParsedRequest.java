package webserver;

import lombok.Getter;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Getter
public class ParsedRequest {

    private String httpMethod;
    private String url;
    private String protocol;
    private Map<String, String> parsedQueryString;
    private Map<String, String> parsedCookies;

    public ParsedRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        parseRequestLine(br);
        parseQueryString(url);
        parseHeader(br);
    }

    private void parseRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] headerTokens = line.split(" ");
        this.httpMethod = headerTokens[0];
        this.url = headerTokens[1];
        this.protocol = headerTokens[2];
    }

    private void parseHeader(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!line.equals("")) {
            line = br.readLine();
        }
    }

    private void parseQueryString(String url) {
        if (url.split("\\?").length >= 2) {
            this.url = url.split("\\?")[0];
            String queryString = url.split("\\?")[1];
            parsedQueryString = HttpRequestUtils.parseQueryString(queryString);
        }
    }
}
