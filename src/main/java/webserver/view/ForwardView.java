package webserver.view;

import exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import util.MimeParser;
import util.TemplateUtils;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.model.Model;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Slf4j
public class ForwardView implements View {

    private final String path;
    private final HttpStatus status;
    private HttpResponse response;
    private Model model;
    private OutputStream os;

    public ForwardView(String viewPath, HttpStatus status) {
        this.path = viewPath;
        this.status = status;
    }

    @Override
    public void render(HttpResponse response, Model model) throws IOException {
        this.response = response;
        this.os = response.getOutputStream();
        this.model = model;

        log.info("forward : {}", path);
        forward();
    }

    private void forward() throws IOException {
        byte[] body = getBytesOfViewFile();
        setContentType();
        writeResponseHeader();
        writeResponseBody(body);
    }

    private void writeResponseHeader() throws IOException {
        os.write(status.getHttpResponseHeader().getBytes());
        os.write(response.getStringOfHeaders().getBytes());
    }

    private void writeResponseBody(byte[] body) throws IOException {
        os.write(body, 0, body.length);
    }

    private byte[] getBytesOfViewFile() throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new NotFoundException("페이지를 찾을 수 없습니다.");
        }

        if (path.endsWith(".html")) {
            return TemplateUtils.parse(file, model);
        }
        return Files.readAllBytes(file.toPath());
    }

    private void setContentType() {
        response.setContentType(MimeParser.parseMimeType(path));
    }

}
