package webserver;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private final String method;
    private final String uri;
    private final List<HttpRequestUtils.Pair> HeaderAttributes;
    private final Map<String,String> parameters;

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String requestLine = br.readLine();
        String[] tokens = requestLine.split(" ");
        this.method = tokens[0];
        this.uri = tokens[1];
        this.parameters = findParameters(uri);
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

    private Map<String,String> findParameters(String uri){
        if(uri.contains("?")){
            return HttpRequestUtils.parseQueryString(uri.split("\\?")[1]);
        }
        return Maps.newHashMap();
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

    public Map<String,String> getParameters() {
        return parameters;
    }
}
