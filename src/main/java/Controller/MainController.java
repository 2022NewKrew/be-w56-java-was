package Controller;

import config.config;
import model.MyHttpRequest;
import model.MyHttpResponse;
import webserver.enums.HttpMethod;
import webserver.enums.HttpStatus;
import webserver.enums.MIME;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public enum MainController implements Controller {
    MAIN_PAGE(HttpMethod.GET, "/") {
        @Override
        public MyHttpResponse run(MyHttpRequest request, DataOutputStream dos) throws IOException {
            return new MyHttpResponse.Builder(dos)
                    .setLocation(config.DEFAULT_URI)
                    .setCookie(request.getHeader("Cookie"))
                    .setContentType(MIME.HTML.getContentType())
                    .setStatus(HttpStatus.OK)
                    .setBody(Files.readAllBytes(new File(config.MAIN_PAGE).toPath()))
                    .build();
        }
    };

    private final HttpMethod method;
    private final String url;

    MainController(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
