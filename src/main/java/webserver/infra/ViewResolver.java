package webserver.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MimeParser;
import webserver.model.HttpStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    private static final String PREFIX_OF_REDIRECTION = "redirect:";
    private static final String NEW_LINE = "\r\n";
    private static final String DEFAULT_PREFIX = "./webapp";

    private static final ViewResolver instance = new ViewResolver();
    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    private ViewResolver() {}

    public static ViewResolver getInstance() {
        if (instance == null) {
            return new ViewResolver();
        }
        return instance;
    }

    public void render(DataOutputStream dos, String viewPath) {
        if (viewPath.startsWith(PREFIX_OF_REDIRECTION)) {
            redirect(dos, viewPath.substring(PREFIX_OF_REDIRECTION.length()));
            return;
        }
        render(dos, viewPath, HttpStatus.OK);
    }

    private void redirect(DataOutputStream dos, String urlPath) {
        try {
            dos.writeBytes(HttpStatus.FOUND.getHttpResponseHeader());
            dos.writeBytes(getLocation(urlPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLocation(String urlPath) {
        return String.format("Location: %s" + NEW_LINE, urlPath);
    }

    public void render(DataOutputStream dos, String viewPath, HttpStatus httpStatus) {
        try {
            byte[] body = getBytesOfFile(viewPath);
            writeResponseHeader(dos, viewPath, httpStatus);
            writeResponseBody(dos, body);
        } catch (IOException e) {
            e.printStackTrace();
            renderNotFound(dos);
        }
    }

    private byte[] getBytesOfFile(String viewPath) throws IOException {
        return Files.readAllBytes(new File(DEFAULT_PREFIX + viewPath).toPath());
    }

    private void writeResponseHeader(DataOutputStream dos, String viewPath, HttpStatus httpStatus) throws IOException {
        dos.writeBytes(httpStatus.getHttpResponseHeader());
        dos.writeBytes(getContentType(viewPath));
        dos.writeBytes(NEW_LINE);
    }

    private String getContentType(String viewPath) {
        return String.format("Content-Type: %s;charset=utf-8" + NEW_LINE, MimeParser.parseMimeType(DEFAULT_PREFIX + viewPath));
    }

    private void writeResponseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
    }

    private void renderNotFound(DataOutputStream dos) {
        render(dos, "/error/404.html", HttpStatus.NOT_FOUND);
    }

    public void renderBadRequest(DataOutputStream dos) {
        render(dos, "/error/400.html", HttpStatus.BAD_REQUEST);
    }

}
