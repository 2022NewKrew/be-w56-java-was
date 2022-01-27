package http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static util.ConstantValues.*;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private final BufferedReader br;

    private String method;
    private String url;
    private Queries requestQueries = null;
    private final RequestHeaders requestHeaders = new RequestHeaders();
    private Queries requestBody;

    public Request(BufferedReader br){
        this.br = br;
    }

    public void read() throws IOException {
        String line = br.readLine();
        readRequestLine(line);
        log.debug("[Request] line : {}", line);
        while(!line.equals("")) {
            line = br.readLine();
            requestHeaders.add(HttpRequestUtils.parseHeader(line));
//            log.debug("[Request] header : {}", line);
        }
        if(requestHeaders.get("Content-Length") != null){
            requestBody = convertString(IOUtils.readData(br, Integer.parseInt(requestHeaders.get("Content-Length"))));
        }
    }

    private void readRequestLine(String line){
        String[] requestConditions = HttpRequestUtils.parseRequestLine(line);
        method = requestConditions[METHOD_IDX];
        String uri = requestConditions[URI_IDX];
        separateQuery(uri);
    }

    private void separateQuery(String requestLine){
        String[] uriSplit = requestLine.split(QUERY_REGEX);
        url = uriSplit[URL_IDX];
        if(uriSplit.length != NO_QUERY){
            requestQueries = convertString(uriSplit[QUERY_IDX]);
        }
    }

    public Queries convertString(String data){
        Queries queries = new Queries(HttpRequestUtils.parseQueryString(URLDecoder.decode(data, StandardCharsets.UTF_8)));
        queries.encode("password");
        return queries;
    }

    public String getMethod() { return  method; }

    public String getUrl(){ return url; }

    public Queries getRequestQueries(){ return requestQueries; }

    public Queries getRequestBody() { return requestBody; }

}
