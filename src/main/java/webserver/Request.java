package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private String method;
    private String uri;
    private List<HttpRequestUtils.Pair> HeaderAttributes = new ArrayList<>();

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String requestLine = br.readLine();
        String[] tokens = requestLine.split(" ");
        this.method = tokens[0];
        this.uri = tokens[1];
        this.HeaderAttributes = findHeaderAttributes(br);
    }

    private List<HttpRequestUtils.Pair> findHeaderAttributes(BufferedReader br) throws IOException {
        List<HttpRequestUtils.Pair> HeaderAttributes = new ArrayList<>();
        String line;
        while(!(line=br.readLine()).equals("")){
            HeaderAttributes.add(HttpRequestUtils.parseHeader(line));
        }
        return HeaderAttributes;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public List<HttpRequestUtils.Pair> getHeaderAttributes() {
        return HeaderAttributes;
    }
}
