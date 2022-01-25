package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestHeader {

    private final RequestLine requestLine;
    private List<String> requestHeader;

    public RequestHeader(BufferedReader br) throws IOException {
        this.requestLine = getRequestLine(br);
        setRequestHeader(br);
    }

    private RequestLine getRequestLine(BufferedReader br) throws IOException {
        String reqLine = br.readLine();
        String[] tokens = reqLine.split(" ");
        return new RequestLine(tokens);
    }

    private void setRequestHeader(BufferedReader br) throws IOException {
        requestHeader = new ArrayList<>();
        String line;
        while(!(line=br.readLine()).equals("")){
            requestHeader.add(line);
        }
    }

    @Override
    public String toString() {
        String output = "\nRequest Line : \n";
        output += this.requestLine.toString();
        output += "\nOthers : \n";
        for(String line : this.requestHeader){
            output += line + "\n";
        }
        return output;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public List<String> getRequestHeader() {
        return requestHeader;
    }
}
