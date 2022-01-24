package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Request {
    public static String DEFAULT_RESOURCE = "/index.html";
    private List<String> requestHeader;
    private String path;
    private HttpMethod method;

    public Request(String requestHeader){
        this.requestHeader = Arrays.asList(requestHeader.split("\n"));
        parsePath(this.requestHeader.get(0));
        parseMethod(this.requestHeader.get(0));
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

        if(path.equals("/")){
            path = DEFAULT_RESOURCE;
        }
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

    public List<String> getRequestHeader() {
        return requestHeader;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
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
