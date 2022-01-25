package http.request;

import java.io.BufferedReader;
import java.io.IOException;

public class StartLine {

    private String method;

    private Url url;

    private String httpVersion;

    public StartLine(String method, Url url, String httpVersion) {
        this.method = method;
        this.url = url;
        this.httpVersion = httpVersion;
    }

    public static StartLine createStartLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] elements = line.split(" ");
        return new StartLine(elements[0], Url.of(elements[1]), elements[2]);
    }

    public String getMethod() {
        return method;
    }

    public Url getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
