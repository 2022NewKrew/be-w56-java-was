package http.response;

import handler.result.HandlerResult;
import handler.result.ModelAndView;
import handler.result.Redirection;
import handler.result.View;
import http.common.Headers;
import http.common.HttpVersion;
import http.common.Mime;
import http.request.HttpRequest;
import template.ViewResolver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class HttpResponse {
    private static final String ERROR_VIEW_URI = "./webapp/error.html";

    private static final String HTTP_RESPONSE_STATUS_LINE_DELIMITER = " ";
    private static final String HTTP_RESPONSE_LINE_DELIMITER = "\r\n";

    private final HttpVersion httpVersion;
    private final Status status;
    private final Headers headers;
    private final ResponseBody responseBody;

    public HttpResponse(HttpVersion httpVersion, Status status, Headers headers, ResponseBody responseBody) {
        this.httpVersion = httpVersion;
        this.status = status;
        this.headers = headers;
        this.responseBody = responseBody;
    }

    public static HttpResponse fromStaticRequest(HttpRequest request) throws IOException {
        HttpVersion httpVersion = HttpVersion.HTTP_1_0;
        String viewName = request.getUri().getValue();
        if("/".equals(viewName) || viewName.isBlank()) {
            viewName = "/index.html";
        }

        try {
            ResponseBody body = ViewResolver.resolveStaticFile(viewName);
            Status status = Status.OK;
            Headers httpHeaders = Headers.withBodyOf(body.getLength(), Mime.fromFileName(viewName));

            return new HttpResponse(httpVersion, status, httpHeaders, body);
        } catch (NoSuchFileException e) {
            return error(Status.NOT_FOUND);
        }
    }

    public static HttpResponse fromHandlerResult(HandlerResult handlerResult) throws IOException {
        if (handlerResult.getClass() == Redirection.class) {
            return buildRedirectionResponse((Redirection) handlerResult);
        }

        if (handlerResult.getClass() == ModelAndView.class) {
            return buildModelAndViewResponse((ModelAndView) handlerResult);
        }

        if (handlerResult.getClass() == View.class) {
            return buildViewResponse((View) handlerResult);
        }

        throw new IOException();
    }

    private static HttpResponse buildRedirectionResponse(Redirection redirection) {
        HttpVersion httpVersion = HttpVersion.HTTP_1_0;
        Status status = Status.FOUND;

        Headers httpHeaders = Headers.redirection(redirection.getUri());
        httpHeaders.addCookies(redirection.getCookies());

        return new HttpResponse(httpVersion, status, httpHeaders, ResponseBody.empty());
    }

    private static HttpResponse buildModelAndViewResponse(ModelAndView modelAndView) {
        HttpVersion httpVersion = HttpVersion.HTTP_1_0;
        Status status = modelAndView.getStatus();
        Mime mime = Mime.fromFileName(modelAndView.getViewName());

        ResponseBody body = ViewResolver.resolveModelAndView(modelAndView);

        Headers httpHeaders = Headers.withBodyOf(body.getLength(), mime);
        httpHeaders.addCookies(modelAndView.getCookies());

        return new HttpResponse(httpVersion, status, httpHeaders, body);
    }

    private static HttpResponse buildViewResponse(View view) {
        HttpVersion httpVersion = HttpVersion.HTTP_1_0;
        Status status = view.getStatus();
        Mime mime = Mime.fromFileName(view.getViewName());

        ResponseBody body = ViewResolver.resolveView(view);

        Headers httpHeaders = Headers.withBodyOf(body.getLength(), mime);
        httpHeaders.addCookies(view.getCookies());

        return new HttpResponse(httpVersion, status, httpHeaders, body);
    }

    public static HttpResponse error(Status status) throws IOException {
        HttpVersion httpVersion = HttpVersion.HTTP_1_0;
        byte[] viewByteArray;

        viewByteArray = Files.readAllBytes(new File(ERROR_VIEW_URI).toPath());

        ResponseBody body = ResponseBody.of(viewByteArray);
        Headers httpHeaders = Headers.withBodyOf(body.getLength(), Mime.HTML);

        return new HttpResponse(httpVersion, status, httpHeaders, body);
    }

    public void send(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        writeStatusLine(dos);
        writeHeaders(dos);
        writeEmptyLine(dos);
        writeBody(dos);

        dos.flush();
    }

    private void writeStatusLine(DataOutputStream dos) throws IOException {
        dos.writeBytes(String.format("%s%s%d%s%s%s",
                httpVersion,
                HTTP_RESPONSE_STATUS_LINE_DELIMITER,
                status.getCode(),
                HTTP_RESPONSE_STATUS_LINE_DELIMITER,
                status.getText(),
                HTTP_RESPONSE_LINE_DELIMITER));
    }

    private void writeHeaders(DataOutputStream dos) throws IOException {
        dos.writeBytes(headers.getFormattedHeader());
    }

    private void writeBody(DataOutputStream dos) throws IOException {
        dos.write(responseBody.getContent(), 0, responseBody.getLength());
    }

    private void writeEmptyLine(DataOutputStream dos) throws  IOException {
        dos.writeBytes(HTTP_RESPONSE_LINE_DELIMITER);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "version=" + httpVersion +
                ", status=" + status +
                ", headers=" + headers +
                ", responseBody=" + responseBody +
                '}';
    }
}
