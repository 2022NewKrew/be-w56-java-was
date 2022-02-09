package http.request;

import http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static util.ConstantValues.*;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private final BufferedReader br;

    private HttpMethod method;
    private String url;
    private Queries requestQueries = null;
    private final RequestHeaders requestHeaders = new RequestHeaders();
    private Queries requestBody;

    public Request(BufferedReader br){
        this.br = br;
    }

    /**
     * HTTP Request 메세지를 읽는 메서드
     * @throws IOException
     */
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

    /**
     * HttpRequest 메세지의 첫 번째 줄을 읽어오는 메서드
     * @param line HttpRequest의 첫 번째 줄
     */
    private void readRequestLine(String line){
        String[] requestConditions = HttpRequestUtils.parseRequestLine(line);
        method = HttpMethod.valueOf(requestConditions[METHOD_IDX]);
        String uri = requestConditions[URI_IDX];
        separateQuery(uri);
    }

    /**
     * URI를 분석하는 메서드, GET 방식인 경우, URI에 Query가 붙어서 오는 경우를 확인하여 저장
     * @param line URI
     */
    private void separateQuery(String line){
        String[] uriSplit = line.split(QUERY_REGEX);
        url = uriSplit[URL_IDX];
        if(uriSplit.length != NO_QUERY){
            requestQueries = convertString(uriSplit[QUERY_IDX]);
        }
    }

    /**
     * URLDecode를 통해 읽은 데이터를 저장하는 메서드
     * @param data raw data
     * @return 저장된 쿼리
     */
    public Queries convertString(String data){
        Queries queries = new Queries(HttpRequestUtils.parseQueryString(URLDecoder.decode(data, StandardCharsets.UTF_8)));
        queries.encode("password");
        return queries;
    }

    public HttpMethod getMethod() { return  method; }

    public String getUrl(){ return url; }

    public Queries getRequestQueries(){ return requestQueries; }

    public Queries getRequestBody() { return requestBody; }

    public boolean isStatic(){
        return Files.isRegularFile(Path.of(ROOT_DIRECTORY + url));
    }

    public String getFirstUrl(){
        String[] urlSplit = url.split(URL_REGEX);
        return urlSplit.length > 0 ? URL_REGEX + urlSplit[CONTROLLER_MATCH_IDX] : URL_REGEX;
    }

    public boolean isLogin(){
        return requestHeaders.isLogin();
    }

}
