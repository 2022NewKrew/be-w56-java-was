package webserver.web.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class Request {

    private Method method;
    private Url url;
    private Header header;
    private Body body;

    private Request(Method method, Url url, Header header, Body body) {
        this.method = method;
        this.url = url;
        this.header = header;
        this.body = body;
    }

    public static Request getRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        List<String> request = new ArrayList<>();
        String startLine = reader.readLine();
        //log.debug("start-line : {}", startLine);
        String s = " ";
        while (!s.equals("")) {
            s = reader.readLine();
            request.add(s);
            //log.debug("{}", s);
        }

        return new Request(Method.getMethod(startLine),
                Url.getUrl(startLine),
                new Header(request),
                new Body(request));
    }
}
