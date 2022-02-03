package webserver.web.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.IOUtils;
import webserver.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class Request {

    private final Method method;
    private final Url url;
    private final Header header;
    private final Body body;
    private final Model model;

    private Request(Method method, Url url, Header header, Body body) {
        this.method = method;
        this.url = url;
        this.header = header;
        this.body = body;
        this.model = new Model();
    }

    public static Request getRequest(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String startLine = reader.readLine();
        Header header = initiateHeader(reader);
        Body body = initiateBody(reader, header);

        return new Request(Method.getMethod(startLine),
                Url.getUrl(startLine),
                header,
                body);
    }

    private static Header initiateHeader(BufferedReader reader) throws IOException {
        List<String> header = new ArrayList<>();
        String s = " ";
        while (!s.equals("")) {
            s = reader.readLine();
            header.add(s);
        }
        return new Header(header);
    }

    private static Body initiateBody(BufferedReader reader, Header header) throws IOException {
        String contentType = header.getHeaders().get("Content-Type");
        if (contentType == null)
            return new Body();
        if (contentType.equals("application/json")) {
            return readJsonFormat(reader, Integer.parseInt(header.getHeaders().get("Content-Length")));
        }
        if (contentType.equals("application/x-www-form-urlencoded")) {
            return readFormUrlFormat(reader, Integer.parseInt(header.getHeaders().get("Content-Length")));
        }
        return new Body();
    }

    private static Body readFormUrlFormat(BufferedReader reader, int length) throws IOException {
        String data = IOUtils.readData(reader, length);
        Body body = new Body();
        body.putUrlEncodedBody(data);
        return body;
    }

    private static Body readJsonFormat(BufferedReader reader, int length) throws IOException {
        return new Body();
    }

    public String inquireHeaderData(String name) {
        return header.findHeaderData(name);
    }

    /*public Object getParameterData(String target, Class type) {
        if (type.equals(Model.class)) {
            return model;
        }
        String data = url.getParameterData(target);
        if (type.equals(Integer.class)) {
            return Integer.parseInt(data);
        }
        if (type.equals(Long.class)) {
            return Long.parseLong(data);
        }
        return data;
    }

    public Object getBodyData(String target, Class type) {
        if (type.equals(Model.class))
            return model;
        String data = body.getData(target);
        if (type.equals(Integer.class)) {
            return Integer.parseInt(data);
        }
        if (type.equals(Long.class)) {
            return Long.parseLong(data);
        }
        return data;
    }*/
}
