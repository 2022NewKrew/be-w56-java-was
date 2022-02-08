package webserver;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private final String method;
    private final String uri;
    private final Map<String,String> header;
    private final Map<String,String> parameters = new HashMap<>();
    private final Map<String,String> cookies;

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String requestLine = URLDecoder.decode(br.readLine(), "UTF-8");
        this.method = requestLine.split(" ")[0];
        this.uri = findUri(requestLine);
        this.parameters.putAll(findQueryString(requestLine));
        this.header = findHeaderParameters(br);
        this.parameters.putAll(findBodyParameters(method, br, Integer.parseInt(header.getOrDefault("Content-Length", "-1"))));
        this.cookies = findCookies(header);
    }

    private Map<String,String> findHeaderParameters(BufferedReader br) throws IOException {
        List<HttpRequestUtils.Pair> HeaderAttributes = new ArrayList<>();
        String line;
        while(!(line=URLDecoder.decode(br.readLine(), "UTF-8")).equals("")){
            HeaderAttributes.add(HttpRequestUtils.parseHeader(line));
        }
        return HeaderAttributes.stream().filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    private Map<String,String> findQueryString(String requestLine){
        if(requestLine.contains("?")){
            return HttpRequestUtils.parseQueryString(requestLine.split(" ")[1].split("\\?")[1]);
        }
        return Maps.newHashMap();
    }

    private String findUri(String requestLine){
        if (requestLine.contains("?")){
            return requestLine.split(" ")[1].split("\\?")[0];
        }
        return requestLine.split(" ")[1];
    }

    private Map<String,String> findBodyParameters(String method, BufferedReader br, int contentLength) throws IOException {
        if(method.equals("GET") || contentLength == -1){
            return Maps.newHashMap();
        }
        return HttpRequestUtils.parseQueryString(URLDecoder.decode(IOUtils.readData(br, contentLength), "UTF-8"));
    }

    private Map<String,String> findCookies(Map<String,String> header){
        if(header.containsKey("Cookie")){
            return HttpRequestUtils.parseCookies(header.get("Cookie"));
        }
        return new HashMap<>();
    }

    public String getParameter(String key){
        return parameters.getOrDefault(key, null);
    }

    public String getCookie(String key){
        return cookies.getOrDefault(key, null);
    }
}
