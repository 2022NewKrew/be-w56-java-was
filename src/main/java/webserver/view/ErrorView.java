package webserver.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BaseException;
import webserver.model.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public class ErrorView implements View {

    private static final Logger log = LoggerFactory.getLogger(ErrorView.class);

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
