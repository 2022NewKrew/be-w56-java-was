package webserver.view;

import lombok.extern.slf4j.Slf4j;
import webserver.model.HttpResponse;
import webserver.model.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class RedirectView implements View {

    private final String path;
    private final HttpStatus status;
    private HttpResponse response;
    private OutputStream os;

    public RedirectView(String viewPath) {
        this.path = viewPath;
        this.status = HttpStatus.FOUND;
    }

    @Override
    public void render(HttpResponse response) throws IOException {
        this.response = response;
        this.os = response.getOutputStream();

        log.info("redirect : {}", path);
        redirect();
    }

    private void redirect() throws IOException {
        response.setLocation(path);
        writeResponseHeader();
    }

    private void writeResponseHeader() throws IOException {
        os.write(status.getHttpResponseHeader().getBytes());
        os.write(response.getStringOfHeaders().getBytes());
    }

}
