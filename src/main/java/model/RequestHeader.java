package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestHeader {

    private final RequestLine requestLine;
    private Map<String, String> requestHeader;

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
        requestHeader = new HashMap<>();
        String line;
        while (!(line = br.readLine()).equals("")) {
            String[] splittedLine = line.split(":");
            requestHeader.put(splittedLine[0], splittedLine[1]);
        }
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "requestLine=" + requestLine +
                ", requestHeader=" + requestHeader +
                '}';
    }
}
