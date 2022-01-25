package http;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Request {
    public static String DEFAULT_RESOURCE = "/index.html";
    private List<String> requestHeader;
    private String path;
    private HttpMethod method;
    private Map<String, String> elements;

    public Request(String requestHeader){
        this.requestHeader = Arrays.asList(requestHeader.split("\n"));
        parsePath(this.requestHeader.get(0));
        parseMethod(this.requestHeader.get(0));
        parseElementsFromGET(this.requestHeader.get(0));
    }


    public static String inputStreamToString(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String str;

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        while( !((str = br.readLine()) == null) && !str.equals("") ){
            sb.append(str + "\n");
        }

        return sb.toString();
    }

    private void parsePath(String lineStr){
        String[] strArray = lineStr.split(" ");
        path = strArray[1];

        //path에서 get방식의 elements들이 전달되는경우 '?' 까지 substring으로 만든다.
        for(int i = 0 ; i < path.length() ; i++){
            if(path.charAt(i) == '?'){
                path = path.substring(0, i);
                break;
            }
        }

        if(path.equals("/")){
            path = DEFAULT_RESOURCE;
        }
    }

    public Map<String, String> getElements() {
        return elements;
    }

    private void parseMethod(String lineStr){
        String[] strArray = lineStr.split(" ");
        String methodStr = strArray[0];

        switch (methodStr){
            case "GET":
                method = HttpMethod.GET;
                break;
            case "POST":
                method = HttpMethod.POST;
                break;
            case "PUT":
                method = HttpMethod.PUT;
                break;
            case "DELETE":
                method = HttpMethod.DELETE;
                break;
            default:
                method = HttpMethod.NONE;
                break;
        }
    }

    private void parseElementsFromGET(String lineStr){
        if(!method.equals(HttpMethod.GET)){
            return;
        }

        //'?' 이후 파싱.
        String elementSubString = divideElementSubString(lineStr);
        this.elements = HttpRequestUtils.parseQueryString(elementSubString);
    }

    public List<String> getRequestHeader() {
        return requestHeader;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    private String divideElementSubString(String lineStr){

        //parse substring after the first question mark.
        for(int i = 0 ; i < lineStr.length() ; i++){
            if(lineStr.charAt(i) == '?'){
                return lineStr.substring(i + 1, lineStr.length());
            }
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String line : requestHeader){
            sb.append(line + "\n");
        }
        return sb.toString();
    }
}
