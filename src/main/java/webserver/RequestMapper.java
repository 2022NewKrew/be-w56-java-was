package webserver;

import webserver.http.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
            response404NotFound(dos);
            return;
        }
        requestMap.get(path).handle(in, dos);
    }

    private static void response404NotFound(DataOutputStream dos) {
        byte[] body = "404 - NOT FOUND!".getBytes();

        MyHttpResponse response = MyHttpResponse.builder(dos)
                .status(HttpStatus.NOT_FOUND)
                .body(body)
                .build();

        response.writeBytes();
        response.flush();
    }

    public enum RequestMapping {
        ROOT("/") {
            @Override
            public void handle(MyHttpRequest request, DataOutputStream dos) {
                byte[] body = "Hello World".getBytes();

                MyHttpResponse response = MyHttpResponse.builder(dos)
                        .status(HttpStatus.OK)
                        .body(body)
                        .build();

                response.writeBytes();
                response.flush();
            }
        },
        INDEX("/index.html") {
            @Override
            public void handle(MyHttpRequest request, DataOutputStream dos) {
                // TODO - 구현 예정
            }
        };

        final String path;

        RequestMapping(String path) {
            this.path = path;
        }

        public abstract void handle(MyHttpRequest request, DataOutputStream dos);
    }
}
