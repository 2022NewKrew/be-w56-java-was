package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class RequestHeader {
    public static String DEFAULT_RESOURCE = "/index.html";
    private List<String> requestHeader;
    private String path;

    public RequestHeader(String requestHeader){
        this.requestHeader = Arrays.asList(requestHeader.split("\n"));
        parsePath(this.requestHeader.get(0));
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

    public List<String> getRequestHeader() {
        return requestHeader;
    }

    public String getPath() {
        return path;
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
