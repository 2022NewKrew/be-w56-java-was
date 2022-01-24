package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;

@Getter
public class HttpRequest {
    private String method;
    private String url;
    private String version;
    private Map<String, String> params;
    private Map<String, String> headers;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String requestLine = br.readLine();
        setRequestLine(requestLine);
        setHeaders(br);
    }

    private void setRequestLine(String requestLine) {
        StringTokenizer st = new StringTokenizer(requestLine);

        method = st.nextToken();
        url = st.nextToken();       //todo params 셋팅해주기
        version = st.nextToken();
    }

    private void setHeaders(BufferedReader br) throws IOException {
        headers = new HashMap<>();

        String line = br.readLine();

        while (StringUtils.isNotEmpty(line)) {
            StringTokenizer st = new StringTokenizer(line, ":");
            headers.put(st.nextToken().trim(), st.nextToken().trim());
            line = br.readLine();
        }
    }
}
