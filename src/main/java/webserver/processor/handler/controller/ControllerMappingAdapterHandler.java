package webserver.processor.handler.controller;

import com.google.common.base.Preconditions;
import http.HttpHandler;
import http.HttpRequest;
import http.HttpResponse;
import webserver.HttpFactory;
import webserver.exception.InternalServerErrorException;
import webserver.http.HttpEntityConverter;
import webserver.http.RequestEntity;
import webserver.http.ResponseEntity;
import webserver.http.exception.ExceptionResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

public class ControllerMappingAdapterHandler implements HttpHandler {

    private final List<ControllerMethod> controllerMethods;
    private final HttpEntityConverter httpEntityConverter;
    private final List<ExceptionResolver> exceptionResolvers;

    public ControllerMappingAdapterHandler(List<Controller> controllers, List<ExceptionResolver> exceptionResolvers) {
        this.controllerMethods = makeControllerMethod(controllers);
        this.httpEntityConverter = HttpFactory.httpEntityConverter();
        this.exceptionResolvers = exceptionResolvers;
    }

    @Override
    public boolean supports(HttpRequest httpRequest) {
        return findControllerMethod(httpRequest) != null;
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) throws Throwable {
        ResponseEntity<?> responseEntity = null;
        try {
            ControllerMethod controllerMethod = findControllerMethod(httpRequest);
            checkNotNull(controllerMethod, "요청을 처리할 수 있는 Controller Method 가 없습니다.");
            Type methodArgumentInnerType = controllerMethod.getMethodArgumentGenericInnerType();
            RequestEntity<?> requestEntity = httpEntityConverter.convertRequest(httpRequest, methodArgumentInnerType);
            responseEntity = controllerMethod.handle(requestEntity);
        } catch (Throwable e) {
            ExceptionResolver exceptionResolver = findExceptionResolverOrElseThrow(e);
            responseEntity = exceptionResolver.resolve(e);
        }
        return httpEntityConverter.convertResponse(responseEntity);
    }

    private ExceptionResolver findExceptionResolverOrElseThrow(Throwable e) throws Throwable {
        for(ExceptionResolver resolver : exceptionResolvers) {
            if(resolver.supports(e)) {
                return resolver;
            }
        }
        throw e;
    }

    private ControllerMethod findControllerMethod(HttpRequest request) {
        for(ControllerMethod controllerMethod : controllerMethods) {
            if(controllerMethod.supports(request)) {
                return controllerMethod;
            }
        }
        return null;
    }

    private List<ControllerMethod> makeControllerMethod(List<Controller> controllers) {
        List<ControllerMethod> controllerMethods = new ArrayList<>();
        for(Controller controller : controllers) {
            controllerMethods.addAll(getControllerMethods(controller));
        }
        return controllerMethods;
    }

    private List<ControllerMethod> getControllerMethods(Controller controller) {
        Class<? extends Controller> clazz = controller.getClass();
        Method[] methods = clazz.getMethods();

        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Request.class))
                .map(method -> new ControllerMethod(controller, method, method.getAnnotation(Request.class)))
                .collect(Collectors.toList());
    }
}
