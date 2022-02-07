package webserver;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import webapp.controller.UserController;
import webserver.handler.HandlerMapper;
import webserver.handler.HandlerMatcher;
import webserver.handler.HandlerParamParser;
import webserver.handler.WrappedHandler;
import webserver.handler.annotation.Controller;
import webserver.handler.annotation.Param;
import webserver.handler.annotation.RequestMapping;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.IntStream;

@Slf4j
public class WebServer {
    private static final int DEFAULT_PORT = 8080;
    private static final Map<HandlerMatcher, WrappedHandler> handlers = new LinkedHashMap<>();

    public static void main(String[] args) throws ClassNotFoundException {
        // Todo: Creation of Dispatcher is hardcoded and need refactoring
        Dispatcher dispatcher = new Dispatcher(createHandlerMapper());
        startListening(getPort(args), dispatcher);
    }

    private static HandlerMapper createHandlerMapper() {
        scanHandlers();
        return new HandlerMapper(handlers);
    }

    private static int getPort(String[] args) {
        return args == null || args.length == 0 ? DEFAULT_PORT : Integer.parseInt(args[0]);
    }

    private static void startListening(int port, Dispatcher dispatcher) {
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started on port {}", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                ConnectionThread connectionThread = new ConnectionThread(connection, dispatcher);
                connectionThread.start();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void scanHandlers() {
        // Todo: Remove magic literal "webapp"
        Reflections reflections = new Reflections("webapp");
        Set<Class<?>> controllers = reflections.get(Scanners.TypesAnnotated.with(Controller.class).asClass());

        log.debug("Detected controllers: {}", controllers);

        for (var controller : controllers) {
            registerController(controller);
        }
    }

    private static void registerController(Class<?> controller) {
        RequestMapping commonRequestMapping = controller.getAnnotation(RequestMapping.class);
        String prefixUrl = commonRequestMapping == null ? "" : commonRequestMapping.value();
        for (var method : controller.getMethods()) {
            registerIfIsHandler(prefixUrl, method);
        }
    }

    private static void registerIfIsHandler(String prefixUrl, Method method) {
        if (method.getAnnotation(RequestMapping.class) != null) {
            registerHandler(prefixUrl, method);
        }
    }

    private static void registerHandler(String prefixUrl, Method method) {
        HandlerMatcher matcher = generateHandlerMatcher(prefixUrl, method);
        WrappedHandler wrappedHandler = generateWrappedHandler(prefixUrl, method);
        handlers.put(matcher, wrappedHandler);
    }

    private static HandlerMatcher generateHandlerMatcher(String prefixUrl, Method method) {
        String path = prefixUrl + method.getAnnotation(RequestMapping.class).value();

        Map<String, Class<?>> requiredParams = new HashMap<>();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int nthParam = 0; nthParam < annotations.length; nthParam++) {
            // Among annotations on each parameter,
            // find the first @Param annotation if exists and register them as required params with their type.
            Class<?> paramType = method.getParameterTypes()[nthParam];
            Annotation[] annotationsOnParam = annotations[nthParam];
            OptionalInt indexOfFirstParamAnnotation = IntStream.range(0, annotationsOnParam.length).filter(
                    nthAnnotation -> annotationsOnParam[nthAnnotation] instanceof Param).findFirst();
            indexOfFirstParamAnnotation.ifPresent(parameter -> requiredParams.put(
                    ((Param) annotationsOnParam[indexOfFirstParamAnnotation.getAsInt()]).value(),
                    paramType));
        }

        return new HandlerMatcher(path, requiredParams);
    }

    private static WrappedHandler generateWrappedHandler(String prefixUrl, Method method) {
        // Generate a wrapped handler properly calls the method defined in controller.
        // That will require some extra preprocessing on the request. e.g. Extracting querystring, parsing them and passing them as arguments.
        return new WrappedHandler(new UserController(), method, new HandlerParamParser());
    }
}
