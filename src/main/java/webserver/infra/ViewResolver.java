package webserver.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.MimeParser;
import webserver.model.HttpResponse;
import webserver.model.HttpStatus;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {
    private static final String NEW_LINE = "\r\n";
    private static final String DEFAULT_PREFIX = "./webapp";

    private static final ViewResolver instance = new ViewResolver();
    private static final Logger log = LoggerFactory.getLogger(ViewResolver.class);

    private final HttpResponse responseToIndex = new HttpResponse("/index.html");
    private final HttpResponse response404 = new HttpResponse("/error/404.html");

    private ViewResolver() {}

    public static ViewResolver getInstance() {
        if (instance == null) {
            return new ViewResolver();
        }
        return instance;
    }

    public void render(DataOutputStream dos, HttpResponse response) {
        if (response.isRedirect()) {
            redirect(dos, response);
            return;
        }
        render(dos, response, HttpStatus.OK);
    }

    private void redirect(DataOutputStream dos, HttpResponse response) {
        try {
            writeRedirectHeader(dos, response.getRedirectUrl());
            writeCookies(dos, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeRedirectHeader(DataOutputStream dos, String urlPath) throws IOException {
        dos.writeBytes(HttpStatus.FOUND.getHttpResponseHeader());
        dos.writeBytes(getLocation(urlPath));
    }

    private String getLocation(String urlPath) {
        return String.format("Location: %s" + NEW_LINE, urlPath);
    }

    public void render(DataOutputStream dos, HttpResponse response, HttpStatus httpStatus) {
        String viewPath = response.getViewPath();
        try {
            byte[] body = getBytesOfFile(viewPath);
            writeResponseHeader(dos, viewPath, httpStatus);
            writeCookies(dos, response);
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

    private void writeCookies(DataOutputStream dos, HttpResponse response) throws IOException {
        if (response.hasCookies()) {
            dos.writeBytes(response.getHttpHeaderOfSetCookie());
        }
    }

    private void writeResponseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
    }

    private void renderNotFound(DataOutputStream dos) {
        render(dos, response404, HttpStatus.NOT_FOUND);
    }

    public void renderExceptionPage(DataOutputStream dos, Exception exception, HttpStatus httpStatus) {
        try {
            byte[] body = exception.getMessage().getBytes();
            writeResponseHeader(dos, httpStatus);
            writeResponseBody(dos, body);
        } catch (IOException e) {
            e.printStackTrace();
            redirect(dos, responseToIndex);
        }
    }

    private void writeResponseHeader(DataOutputStream dos, HttpStatus httpStatus) throws IOException {
        dos.writeBytes(httpStatus.getHttpResponseHeader());
        dos.writeBytes(getTextContentType());
        dos.writeBytes(NEW_LINE);
    }

    private String getTextContentType() {
        return "Content-Type: text/html;charset=utf-8" + NEW_LINE;
    }

}
