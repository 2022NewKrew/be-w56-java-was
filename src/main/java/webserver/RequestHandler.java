package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;
import webserver.http.HttpCodec;
import webserver.http.connection.SocketIO;
import webserver.http.message.HttpRequest;
import webserver.http.message.HttpResponse;
import webserver.http.message.values.HttpContentType;
import webserver.http.message.values.HttpMethod;
import webserver.http.message.values.HttpResponseStatus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Controller controller;
    private final Socket connection;

    public RequestHandler(Controller controller, Socket connectionSocket) {
        this.controller = controller;
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        final HttpCodec httpCodec = new HttpCodec(new SocketIO(connection));

        final HttpRequest request = httpCodec.decode();
        final HttpResponse response = new HttpResponse(request);
        mapToController(response);

        httpCodec.encode(response);
    }

    private void mapToController(HttpResponse response) {
        final HttpRequest request = response.getRequest();
        final HttpMethod requestMethod = request.getMethod();

        final Method[] methods = controller.getClass().getDeclaredMethods();

        switch (requestMethod) {
            case GET:
                mapToGetMappingAnnotation(methods, response);
                break;
            case POST:
                mapToPostMappingAnnotation(methods, response);
                break;
        }
    }

    private void mapToGetMappingAnnotation(Method[] methods, HttpResponse response) {
        final HttpRequest request = response.getRequest();
        final String requestUri = request.getUri();
        final Optional<Method> mappedMethod = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(GetMapping.class))
                .filter(method -> method.getDeclaredAnnotation(GetMapping.class).value().equals(requestUri))
                .findFirst();

        mappedMethod.ifPresentOrElse(method -> doMethod(method, response), () -> setResponseUri(response ,requestUri));
    }

    private void doMethod(Method method, HttpResponse response) {
        try {
            final String responseUri = (String) method.invoke(controller, response);
            setResponseUri(response, responseUri);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void setResponseUri(HttpResponse response, String responseUri) {
        if(responseUri.contains("redirect:")) {
            int idx = responseUri.indexOf(":");
            responseUri = responseUri.substring(idx + 1);

            response.setStatus(HttpResponseStatus.FOUND);
        }
        else {
            response.setStatus(HttpResponseStatus.OK);
        }

        try {
            final byte[] body = Files.readAllBytes(new File("./webapp" + responseUri).toPath());
            response.setContentType(HttpContentType.getHttpContentType(responseUri));
            response.setResponseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mapToPostMappingAnnotation(Method[] methods, HttpResponse response) {
        final HttpRequest request = response.getRequest();
        final String requestUri = request.getUri();
        final Optional<Method> mappedMethod = Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(PostMapping.class))
                .filter(method -> method.getDeclaredAnnotation(PostMapping.class).value().equals(requestUri))
                .findFirst();

        mappedMethod.ifPresentOrElse(method -> doMethod(method, response), () -> setResponseUri(response, requestUri));
    }
}
