package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {
    private static final String REGEX = "\\?";

    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private final InputStream in;
    private final RequestLines requestLines = new RequestLines();
    private final RequestHeaders requestHeaders = new RequestHeaders();
    private Queries queries;

    public Request(InputStream in){
        this.in = in;
    }

    public void read() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        requestLines.parseRequestLine(line);
        log.debug("[Request] line : {}", line);
        separateQuery(requestLines.getUrl());
        while(!line.equals("")) {
            line = br.readLine();
            requestHeaders.add(HttpRequestUtils.parseHeader(line));
            log.debug("[Request] header : {}", line);
        }
    }

    private void separateQuery(String url){
        String[] urlSplit = url.split(REGEX);
        String queryString = (urlSplit.length == 1) ? null : urlSplit[1];
        queries = new Queries(HttpRequestUtils.parseQueryString(queryString));
    }

    public String getUrl(){ return requestLines.getUrl(); }

    public Queries getQueries(){ return queries; }

}
