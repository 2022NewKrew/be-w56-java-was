package webserver;

import webserver.http.MyHttpRequest;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static util.HttpResponseUtils.response200Header;
import static util.HttpResponseUtils.responseBody;

public class RequestMapper {

    private static final Map<String, RequestMapping> requestMap;

    static {
        requestMap = new HashMap<>();
        for (RequestMapping value : RequestMapping.values()) {
            requestMap.put(value.path, value);
        }
    }

    public static void process(MyHttpRequest in, OutputStream out) {
        String path = in.uri().getPath();
        if (requestMap.containsKey(path)) {
            requestMap.get(path).handle(in, out);
        }
    }

    public enum RequestMapping {
        ROOT("/") {
            @Override
            public void handle(MyHttpRequest in, OutputStream out) {
                DataOutputStream dos = new DataOutputStream(out);
                byte[] body = "Hello World".getBytes();
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        },
        INDEX("/index.html") {
            @Override
            public void handle(MyHttpRequest in, OutputStream out) {
                // TODO - 구현 예정
            }
        };

        final String path;

        RequestMapping(String path) {
            this.path = path;
        }

        public abstract void handle(MyHttpRequest in, OutputStream out);
    }
}
