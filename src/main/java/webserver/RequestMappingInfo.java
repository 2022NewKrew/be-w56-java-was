package webserver;

import webserver.http.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;

public enum RequestMappingInfo {

    ROOT("/") {
        @Override
        public void handle(MyHttpRequest request, DataOutputStream dos) throws Exception {
            byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());

            MyHttpResponse response = MyHttpResponse.builder(dos)
                    .status(HttpStatus.OK)
                    .body(body)
                    .build();

            response.writeBytes();
            response.flush();
        }
    };

    private final String path;

    RequestMappingInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public abstract void handle(MyHttpRequest request, DataOutputStream dos) throws Exception;
}
