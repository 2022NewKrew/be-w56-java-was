package webserver.http.request;

import com.google.common.base.Strings;
import exception.IllegalRequestException;
import util.HttpRequestUtils;

import java.util.Map;

public class RequestLines {
    private static final String LINE_REGEX = " ";
    private static final int METHOD_IDX = 0;
    private static final int URI_IDX = 1;
    private static final String QUERY_REGEX = "\\?";
    private static final int URL_IDX = 0;
    private static final int QUERY_IDX = 1;
    private static final int NO_QUERY = 1;

    private String method;
    private String url;
    private Queries queries;

    public RequestLines(){}

    public void parseRequestLine(String requestLine){
        if(Strings.isNullOrEmpty(requestLine)){
            throw new IllegalRequestException("잘못된 HTTP 요청입니다.");
        }

        String[] tokens = requestLine.split(LINE_REGEX);
        method = tokens[METHOD_IDX];
        String uri = tokens[URI_IDX];
        separateQuery(uri);
    }

    private void separateQuery(String uri){
        String[] uriSplit = uri.split(QUERY_REGEX);
        url = uriSplit[URL_IDX];
        String queryString = (uriSplit.length == NO_QUERY) ? null : uriSplit[QUERY_IDX];
        queries = new Queries(HttpRequestUtils.parseQueryString(queryString));
    }


    public String getMethod() {
        return method;
    }

    public String getUrl(){
        return url;
    }

    public Map<String, String> getQueries() { return queries.getQueries(); }
}
