package controller;

import dynamic.DynamicHtmlBuilder;
import dynamic.DynamicModel;
import exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import model.*;
import service.MemoService;
import util.ControllerUtils;
import util.HttpResponseHeader;
import util.IOUtils;
import util.Links;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@Slf4j
public class RequestController {
    private static final MemoService memoService = MemoService.getInstance();

    private RequestController() {

    }

    public static HttpResponse controlRequest(RequestHeader requestHeader) throws Exception {
        String uri = requestHeader.getHeader("uri");
        String method = requestHeader.getHeader("method");
        log.info("CONTROL URI: " + uri);
        log.info("CONTROL METHOD: " + method);

        DynamicModel model = new DynamicModel();
        HttpResponse httpResponse;

        try {
            httpResponse = UserController.controlRequest(requestHeader, uri, method);
            if (httpResponse == null) {
                httpResponse = MemoController.controlRequest(requestHeader, uri, method);
            }
            if (httpResponse != null) {
                return httpResponse;
            }

            if (uri.startsWith("/error")) {
                return getErrorPage(model);
            }

            if ((uri.equals("/") || uri.startsWith("/index")) && method.equals("GET")) {
                return getIndex(requestHeader, model);
            }

            return getDefault(requestHeader, model);
        } catch (Exception exception) {
            exception.printStackTrace();
            ExceptionHandler.handleException(exception, requestHeader);
            return ControllerUtils.redirect(Links.ERROR);
        }
    }

    private static HttpResponse getIndex(RequestHeader requestHeader, DynamicModel model)
            throws SQLException, IOException {
        model.addAttribute("memo", memoService.findAll());

        return HttpResponseBuilder.build(
                Links.MAIN,
                DynamicHtmlBuilder.getDynamicHtml(IOUtils.readBody(Links.MAIN), model),
                HttpResponseHeader.RESPONSE_200,
                requestHeader.getAccept()
        );
    }

    private static HttpResponse getErrorPage(DynamicModel model) throws IOException {
        return HttpResponseBuilder.build(
                Links.ERROR,
                DynamicHtmlBuilder.getDynamicHtml(IOUtils.readBody(Links.ERROR), model),
                HttpResponseHeader.RESPONSE_200,
                "text/html"
        );
    }

    private static HttpResponse getDefault(RequestHeader requestHeader, DynamicModel model) throws IOException {
        String locationUri = requestHeader.getHeader("uri");

        if (!locationUri.contains(".")) {
            locationUri += ".html";
        }

        byte[] body = IOUtils.readBody(locationUri);
        if (locationUri.contains(".html")) {
            body = DynamicHtmlBuilder.getDynamicHtml(body, model);
        }

        return HttpResponseBuilder.build(
                locationUri,
                body,
                HttpResponseHeader.RESPONSE_200,
                requestHeader.getAccept()
        );
    }
}
