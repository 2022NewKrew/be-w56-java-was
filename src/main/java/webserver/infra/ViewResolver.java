package webserver.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MimeParser;
import webserver.model.HttpStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

    public void render(OutputStream out, String viewPath) {
        if (viewPath.startsWith(PREFIX_OF_REDIRECTION)) {
            redirect(out, viewPath.substring(PREFIX_OF_REDIRECTION.length()));
            return;
        }
        render(out, viewPath, HttpStatus.OK);
    }

    private void redirect(OutputStream out, String urlPath) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeBytes(HttpStatus.FOUND.getHttpResponseHeader());
            dos.writeBytes(getLocation(urlPath));
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLocation(String urlPath) {
        return String.format("Location: %s" + NEW_LINE, urlPath);
    }

    public void render(OutputStream out, String viewPath, HttpStatus httpStatus) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            byte[] body = getBytesOfFile(viewPath);
            writeResponseHeader(dos, viewPath, httpStatus);
            writeResponseBody(dos, body);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            renderNotFound(out);
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

    private void renderNotFound(OutputStream out) {
        render(out, "/error/404.html", HttpStatus.NOT_FOUND);
    }

}
