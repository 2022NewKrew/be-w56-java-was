package webserver.view;

import lombok.extern.slf4j.Slf4j;
import webserver.exception.BaseException;
import webserver.model.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class ErrorView implements View {

    private final BaseException exception;
    private HttpResponse response;
    private OutputStream out;

    public ErrorView(BaseException exception) {
        this.exception = exception;
    }

    @Override
    public void render(HttpResponse response) throws IOException {
        this.response = response;
        this.out = response.getOutputStream();

        log.error(exception.getMessage());
        forward();
    }

    private void forward() throws IOException {
        setContentType();
        writeResponseHeader();
        writeResponseBody();
    }

    private void setContentType() {
        response.setContentType("text/html");
    }

    private void writeResponseHeader() throws IOException {
        out.write(exception.getHttpStatus().getHttpResponseHeader().getBytes());
        out.write(response.getStringOfHeaders().getBytes());
    }

    private void writeResponseBody() throws IOException {
        byte[] body = exception.getMessage().getBytes();
        out.write(body, 0, body.length);
    }

}
