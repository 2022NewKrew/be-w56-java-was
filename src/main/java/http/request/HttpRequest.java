package http.request;

import http.HttpBody;
import http.HttpHeader;
import http.util.HttpRequestUtils;
import http.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequest  {

    private final HttpRequestStartLine startLine;
    private final HttpHeader header;
    private final HttpBody body;

    private HttpRequest(HttpRequestStartLine startLine, HttpHeader header, HttpBody body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public static HttpRequest create(InputStream in) throws IOException {
        return parseRequest(in);
    }

    private static HttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        //startLine
        String line = br.readLine();
        HttpRequestStartLine startLine = new HttpRequestStartLine(line);

        //header
        HttpHeader header = new HttpHeader();
        while(!(line= br.readLine()).equals("")){
            header.addHeader(line);
        }

        //body
        String bodyFromReader = null;
        String contentLength = header.getHeaders().get("Content-Length");
        if(contentLength!=null && Integer.parseInt(contentLength)>0){
            bodyFromReader = IOUtils.readData(br, Integer.parseInt(contentLength));
        }
        HttpBody body = new HttpBody(bodyFromReader);

        return new HttpRequest(
                startLine,
                header,
                body
        );
    }

    public HttpRequestStartLine getStartLine() {
        return startLine;
    }

    public HttpHeader getHttpHeader() {
        return header;
    }

    public HttpBody getHttpBody() {
        return body;
    }
}
