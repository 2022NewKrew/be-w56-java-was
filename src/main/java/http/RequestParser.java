package http;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class RequestParser {
    public static String DEFAULT_RESOURCE = "/index.html";

    private RequestParser() {}

    public static String inputStreamToString(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String str;

        Integer contentLength = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        while( !((str = br.readLine()) == null) && !str.equals("") ){
            if(str.startsWith("Content-Length: ")){
                contentLength = Integer.valueOf(str.substring("Content-Length: ".length(), str.length()));
            }
            sb.append(str + "\n");
        }

        if(contentLength > 0){
            sb.append("START_BODY");
            sb.append(IOUtils.readData(br, contentLength) + "\n");
        }

        return sb.toString();
    }

    public static String parsePath(Request request){
        String[] strArray = request.getRequestHeader().get(0).split(" "); //첫번째 줄을 가져옴.
        String path = strArray[1];

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

        return path;
    }

    public static HttpMethod parseMethod(Request request){
        String[] strArray =  request.getRequestHeader().get(0).split(" ");
        String methodStr = strArray[0];

        switch (methodStr){
            case "GET":
                return HttpMethod.GET;
            case "POST":
                return HttpMethod.POST;
            case "PUT":
                return HttpMethod.PUT;
            case "DELETE":
                return HttpMethod.DELETE;
            default:
                return HttpMethod.NONE;
        }
    }

    public static Map<String, String> parseElementsFromGet(Request request){
        String requestLine = request.getRequestHeader().get(0);

        //'?' 이후 파싱.
        String elementSubString = divideElementSubString(requestLine);
        return HttpRequestUtils.parseQueryString(elementSubString);
    }

    public static Map<String, String> parseElementsFromPost(Request request){
        String requestLine = request.getRequestBody().get(0);
        return HttpRequestUtils.parseQueryString(requestLine);
    }


    private static String divideElementSubString(String lineStr){

        //parse substring after the first question mark.
        for(int i = 0 ; i < lineStr.length() ; i++){
            if(lineStr.charAt(i) == '?'){
                return lineStr.substring(i + 1, lineStr.length());
            }
        }

        return null;
    }
}
