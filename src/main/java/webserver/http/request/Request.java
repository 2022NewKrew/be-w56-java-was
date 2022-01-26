package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private final InputStream in;
    private final RequestLines requestLines = new RequestLines();
    private final RequestHeaders requestHeaders = new RequestHeaders();

    public Request(InputStream in){
        this.in = in;
    }

    public void read() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        requestLines.parseRequestLine(line);
        log.debug("[Request] line : {}", line);
        while(!line.equals("")) {
            line = br.readLine();
            requestHeaders.add(HttpRequestUtils.parseHeader(line));
//            log.debug("[Request] header : {}", line);
        }
    }

    public String getMethod() { return  requestLines.getMethod(); }

    public String getUrl(){ return requestLines.getUrl(); }

    public Map<String, String> getQueries(){ return requestLines.getQueries(); }

}
