package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

@Getter
public class HttpRequestHeader {
    private Map<String, String> headers;

    public HttpRequestHeader(BufferedReader br) throws IOException {
        this.headers = new HashMap<>();

        String line = br.readLine();

        while (StringUtils.isNotEmpty(line)) {
            StringTokenizer st = new StringTokenizer(line, ":");
            headers.put(st.nextToken().trim(), st.nextToken().trim());
            line = br.readLine();
        }
    }
}
