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
        this.method = requestLine.split(" ")[0];
        this.uri = findUri(requestLine);
        this.parameters = findParameters(requestLine);
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

    private Map<String,String> findParameters(String requestLine){
        if(requestLine.contains("?")){
            return HttpRequestUtils.parseQueryString(requestLine.split(" ")[1].split("\\?")[1]);
        }
        return Maps.newHashMap();
    }

    private String findUri(String requestLine){
        log.info(requestLine);
        if (requestLine.contains("?")){
            return requestLine.split(" ")[1].split("\\?")[0];
        }
        return requestLine.split(" ")[1];
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
