package webserver.handler.handlerAdapter;

import static app.config.GlobalConfig.SUFFIX;
import static util.Constant.CONTENT_LENGTH;
import static util.Constant.CONTENT_TYPE;
import static util.Constant.INDEX_PATH;
import static util.Constant.LOCATION;
import static util.Constant.REDIRECT;
import static util.Constant.ROOT_PATH;
import static util.Constant.UTF_8;
import static util.Constant.WEBAPP_PATH;
import static util.Constant.ZERO;
import static util.HttpRequestUtils.parseRedirect;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.exception.CustomException;
import app.http.HttpRequest;
import app.http.HttpResponse;
import app.http.HttpStatus;
import app.http.HttpVersion;
import app.http.Mime;
import webserver.handler.HandlerMethod;
import webserver.handler.typeResolver.HttpResponseTypeResolver;
import webserver.handler.typeResolver.MapTypeResolver;
import webserver.handler.typeResolver.ModelTypeResolver;
import webserver.handler.typeResolver.TypeResolver;
import webserver.template.Model;
import webserver.template.TemplateEngine;

public class DefaultHandlerAdapter implements HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(DefaultHandlerAdapter.class);
    private static final Map<Class<?>, TypeResolver> typeResolver = new HashMap<>();

    static {
        typeResolver.put(Map.class, MapTypeResolver.getInstance());
        typeResolver.put(HttpResponse.class, HttpResponseTypeResolver.getInstance());
        typeResolver.put(Model.class, ModelTypeResolver.getInstance());
    }

    @Override
    public boolean supports(Object handlerMethod) {
        if (handlerMethod != null) {
            return handlerMethod instanceof HandlerMethod;
        }
        return false;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HandlerMethod handlerMethod) throws CustomException {
        try {
            Method method = handlerMethod.getMethod();
            Model model = Model.from();
            List<Object> arguments = getArgument(request, response, handlerMethod, model);
            String path;
            if (arguments == null) {
                path = (String) method.invoke(handlerMethod.getClazz().getConstructor().newInstance());
                setResponse(request, response, path, model);
                return;
            }
            path = (String) method.invoke(handlerMethod.getClazz().getConstructor().newInstance(), arguments.toArray());
            setResponse(request, response, path, model);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | NoSuchMethodException | InstantiationException e) {
            log.error("error during handle: " + e.getClass().getName());
            Throwable exception = e.getCause();
            if(exception instanceof CustomException) {
                throw (CustomException) exception;
            }
        }
    }

    public List<Object> getArgument(HttpRequest request, HttpResponse response, HandlerMethod handlerMethod, Model model) {
        List<Object> arguments = new ArrayList<>();
        Parameter[] parameters = handlerMethod.getMethod().getParameters();
        for (Parameter parameter : parameters) {
            if (typeResolver.containsKey(parameter.getType())) {
                arguments.add(typeResolver.get(parameter.getType())
                                          .getType(request, response, model));
            }
        }
        if (arguments.isEmpty()) {
            return null;
        }
        return arguments;
    }

    private void setResponse(HttpRequest request, HttpResponse response, String path, Model model) {
        if(!path.endsWith(SUFFIX)) {
            path = path + SUFFIX;
        }

        if(path.startsWith(REDIRECT)) {
            path = parseRedirect(path);
            response.put(CONTENT_TYPE, Mime.getMime(path).getContentType() + UTF_8);
            response.setVersion(HttpVersion.HTTP_1_1);
            response.setStatus(HttpStatus.FOUND);
            response.put(LOCATION, path);
            response.setBody(new byte[0]);
            response.put(CONTENT_LENGTH, ZERO);
            return;
        }
        try {
            if (path.equals(ROOT_PATH)) {
                path = INDEX_PATH;
            }
            path = WEBAPP_PATH + path;
            response.put(CONTENT_TYPE, Mime.getMime(path).getContentType() + UTF_8);
            String html = Files.readString(new File(path).toPath());
            if(!model.isEmpty()) {
                html = TemplateEngine.render(html, model);
            }
            byte[] body = html.getBytes(StandardCharsets.UTF_8);
            response.put(CONTENT_LENGTH, String.valueOf(body.length));
            response.setVersion(HttpVersion.HTTP_1_1);
            response.setStatus(HttpStatus.OK);
            response.setBody(body);
        } catch (IOException e) {
            log.error("setResponse IOE Error: " + e.getMessage());
        }
    }
}
