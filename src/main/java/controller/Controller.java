package controller;

import annotation.RequestMapping;
import http.request.HttpRequest;
import http.request.HttpRequestBody;
import http.request.HttpRequestMethod;
import http.response.HttpResponse;
import http.response.HttpResponse.HttpResponseBuilder;
import http.response.HttpResponseHeader;
import http.response.HttpResponseHeader.ResponseHeaderBuilder;
import http.response.HttpResponseHeaderKey;
import http.response.HttpStatusCode;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.UrlUtils;
import webserver.UrlMapper;
import webserver.ViewResolver;

public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private static final String REDIRECT = "redirect:";
    private static final String ERROR = "error:";

    // DO NOT OVERRIDE CONSTRUCTOR !
    public Controller() {
        register();
    }

    public void register() {
        for (Method m: this.getClass().getMethods()) {
            RequestMapping anno = m.getDeclaredAnnotation(RequestMapping.class);
            if (anno != null) {
                String url = anno.value();
                HttpRequestMethod method = anno.method();
                Map<HttpResponseHeaderKey, String> headerMap = new EnumMap<>(
                        HttpResponseHeaderKey.class);

                UrlMapper.put(url, method, (HttpRequest httpRequest) -> {
                    if (m.getParameters().length == 2) {
                        return run(m, httpRequest, headerMap);
                    }
                    return run(m, httpRequest);
                });
            }
        }
    }

    private HttpResponse run(Method m, HttpRequest httpRequest,
            Map<HttpResponseHeaderKey, String> headerMap) {
        try {
            String methodResult = (String) m.invoke(this, httpRequest, headerMap);
            return respond(methodResult, headerMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return render404();
        }
    }

    private HttpResponse run(Method m, HttpRequest httpRequest) {
        Map<HttpResponseHeaderKey, String> emptyMap = new EnumMap<>(HttpResponseHeaderKey.class);
        try {
            String methodResult = (String) m.invoke(this, httpRequest);
            return respond(methodResult, emptyMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return render404();
        }
    }

    private HttpResponse respond(String response, Map<HttpResponseHeaderKey, String> headerMap) {
        if (response.startsWith(REDIRECT)) {
            return redirect(response.substring(REDIRECT.length()), headerMap);
        } else if (response.startsWith(ERROR)) {
            // FIXME -> ERROR_CODE 에 따른 에러 페이지 리턴
            return render404();
        }
        return render(response, headerMap);
    }

    private HttpResponse render(String filePath, Map<HttpResponseHeaderKey, String> headerMap) {
        filePath = ViewResolver.getStaticFilePath(filePath);
        byte[] body = IOUtils.readFile(filePath);

        ResponseHeaderBuilder httpResponseHeaderBuilder = new ResponseHeaderBuilder()
                .set(HttpResponseHeaderKey.CONTENT_LENGTH, "" + body.length);
        headerMap.forEach(httpResponseHeaderBuilder::set);
        HttpResponseHeader httpResponseHeader = httpResponseHeaderBuilder.build();

        if (body.length == 0) {
            return render404();
        }

        return new HttpResponseBuilder(HttpStatusCode.OK)
                .setHeader(httpResponseHeader)
                .setBody(body)
                .build();
    }

    private HttpResponse redirect(String redirectUrl, Map<HttpResponseHeaderKey, String> headerMap) {
        ResponseHeaderBuilder httpResponseHeaderBuilder = new ResponseHeaderBuilder()
                .set(HttpResponseHeaderKey.LOCATION, redirectUrl);
        headerMap.forEach(httpResponseHeaderBuilder::set);
        HttpResponseHeader httpResponseHeader = httpResponseHeaderBuilder.build();

        return new HttpResponseBuilder(HttpStatusCode.FOUND)
                .setHeader(httpResponseHeader)
                .build();
    }

    private HttpResponse render404() {
        return new HttpResponseBuilder(HttpStatusCode.NOT_FOUND).build();
    }

    protected static Map<String, String> getData(HttpRequest httpRequest) {
        HttpRequestBody httpRequestBody = httpRequest.getRequestBody();
        String body = String.valueOf(httpRequestBody.getBody());
        body = UrlUtils.decode(body);
        return IOUtils.getBodyData(body);
    }
}
