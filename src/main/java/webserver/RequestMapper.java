package webserver;

import webserver.http.HttpStatus;
import webserver.http.MyHttpRequest;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static util.HttpResponseUtils.*;

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
        DataOutputStream dos = new DataOutputStream(out);

        if (!requestMap.containsKey(path)) {
            byte[] body = "404 - NOT FOUND!".getBytes();
            responseHeader(HttpStatus.NOT_FOUND, dos, body.length);
            responseBody(dos, body);
            return;
        }
        requestMap.get(path).handle(in, dos);
    }

    public enum RequestMapping {
        ROOT("/") {
            @Override
            public void handle(MyHttpRequest in, DataOutputStream dos) {
                byte[] body = "Hello World".getBytes();
                responseHeader(HttpStatus.OK, dos, body.length);
                responseBody(dos, body);
            }
        },
        INDEX("/index.html") {
            @Override
            public void handle(MyHttpRequest in, DataOutputStream dos) {
                // TODO - 구현 예정
            }
        };

        final String path;

        RequestMapping(String path) {
            this.path = path;
        }

        public abstract void handle(MyHttpRequest in, DataOutputStream dos);
    }
}
