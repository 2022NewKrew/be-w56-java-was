package com.my.was.webserver;

import com.my.was.ControllerScanner;
import com.my.was.container.handleradapters.HandlerAdapter;
import com.my.was.container.handleradapters.RequestMappingHandlerAdapter;
import com.my.was.container.handleradapters.ResourceHandlerAdapter;
import com.my.was.container.handlermappings.HandlerMapping;
import com.my.was.container.handlermappings.RequestMappingHandlerMapping;
import com.my.was.container.handlermappings.ResourceHandlerMapping;
import com.my.was.http.request.HttpRequest;
import com.my.was.http.request.parser.HttpRequestParser;
import com.my.was.http.response.HttpResponse;
import com.my.was.http.response.HttpResponseSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestHandler extends Thread {

    private final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpRequestParser httpRequestParser = new HttpRequestParser();
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private Socket connection;

    /**
     * ResourceHandlerMapping 마지막에 등록한다.
     * @author leaf.hyeon
     */
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        handlerMappings.add(new RequestMappingHandlerMapping());
        handlerMappings.add(new ResourceHandlerMapping());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new RequestMappingHandlerAdapter());
        handlerAdapters.add(new ResourceHandlerAdapter());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = httpRequestParser.parse(in);
            loggingRequestHeader(httpRequest);
            HttpResponse httpResponse = new HttpResponse();

            Object handler = findHandler(httpRequest);
            HandlerAdapter handlerAdapter = findHandlerAdapter(handler);
            handlerAdapter.handle(handler, httpRequest, httpResponse);

            HttpResponseSender httpResponseSender = new HttpResponseSender(httpResponse);
            httpResponseSender.send(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Object findHandler(HttpRequest httpRequest) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.findHandler(httpRequest);
            if (handler != null) {
                return handler;
            }
        }

        return null;
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupported(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("알맞은 핸들러 어댑터가 존재하지 않습니다."));
    }

    private void loggingRequestHeader(HttpRequest httpRequest) {
        for (Map.Entry header : httpRequest.getRequestHeaders().getHeaders().entrySet()) {
            log.info("{} {}", header.getKey(), header.getValue());
        }
    }
}
