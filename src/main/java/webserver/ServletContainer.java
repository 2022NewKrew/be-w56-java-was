package webserver;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.annotation.components.Controller;
import util.annotation.mapping.RequestMapping;
import util.http.HttpMethod;
import util.http.HttpRequest;
import util.http.HttpResponse;
import util.http.HttpResponseUtils;
import util.ui.Model;
import util.ui.ModelImpl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class ServletContainer {
    private static final Logger log = LoggerFactory.getLogger(ServletContainer.class);
    private final Map<Method, Object> methodClassMap;
    private final Map<HttpMethod, Map<String, Method>> httpMethodMapMap;

    // Todo 서블릿 컨테이너에 URL Mapping이 있어야하나? 이건 서블릿의 역할이 아닌가? -> 서블릿과 컨테이너를 나눠 볼 필요가 있다.
    public ServletContainer() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        methodClassMap = new HashMap<>();
        httpMethodMapMap = new EnumMap<>(HttpMethod.class);
        Arrays.stream(HttpMethod.values()).forEach(v -> httpMethodMapMap.put(v, new HashMap<>()));
        // Todo 각 요청마다 새로 만들고있다. 고칠필요가 있음.
        // Todo DI를 여기서 구현해 볼 수 있다.
        Set<Class> s = findAllClassesUsingReflectionsLibrary("app");
        for (Class controller : s.toArray(new Class[0])) {
            if (controller.getAnnotation(Controller.class) == null) continue;
            componentMapping(controller, controller.getMethods());
        }
    }

    private void componentMapping(Class controllerClass, Method[] methods) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.debug("controller detected {}", controllerClass);
        for (Method method : methods) {
            log.debug("method detected : {}, {}", controllerClass.getName(), method.getName());
            methodControllerMapping(method, controllerClass);
            urlMethodMapping(method);
        }
    }

    private void methodControllerMapping(Method method, Class controllerClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        methodClassMap.put(method, controllerClass.getConstructor().newInstance());
    }

    private void urlMethodMapping(Method method) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            RequestMapping requestMapping = annotation.annotationType().getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                String url = (String) annotation.getClass().getMethod("url").invoke(annotation);
                httpMethodMapMap.get(requestMapping.method()).put(url, method);
            }
        }
    }

    private Method methodFromUrl(HttpRequest httpRequest) {
        return httpMethodMapMap.get(httpRequest.method()).get(httpRequest.url());
    }

    private Map<String, String> dataFromRequest(HttpRequest httpRequest) {
        if (httpRequest.method() == HttpMethod.GET)
            return httpRequest.params();
        if (httpRequest.method() == HttpMethod.POST)
            return httpRequest.body();
        return null;
    }

    private List<Object> methodArguments(Method method, Map data, HttpRequest httpRequest, HttpResponse httpResponse) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<Object> arguments = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            arguments.add(methodArgument(parameter, data, httpRequest, httpResponse));
        }
        return arguments;
    }

    private Object methodArgument(Parameter parameter, Map<String, String> data, HttpRequest httpRequest, HttpResponse httpResponse) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (parameter.getType() == Map.class)
            return data;
        if (parameter.getType() == HttpRequest.class)
            return httpRequest;
        if (parameter.getType() == HttpResponse.class)
            return httpResponse;
        if (parameter.getType() == Model.class)
            return new ModelImpl();
        return objectArgument(parameter, data);
    }

    private Object objectArgument(Parameter parameter, Map<String, String> data) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Field[] fields = parameter.getType().getDeclaredFields();
        List<Class> types = new ArrayList<>();
        List<Object> paramList = new ArrayList<>();
        for (Field field : fields) {
            String name = field.getName();
            String value = data.get(name);
            if(value != null) {
                log.debug("valid param : {}", name);
                types.add(field.getType());
                paramList.add(field.getType().cast(value));
            }
        }
        log.debug("paramList.size() : {}", paramList.size());
        return parameter.getType().getConstructor(types.toArray(new Class[0])).newInstance(paramList.toArray());
    }

    private void controllerResponse(String controllerResult, Model model, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String[] controllerResults = controllerResult.split(":");
        if (controllerResults[0].equals("redirect")) {
            HttpResponseUtils.redirectResponse(httpResponse, controllerResults[1].trim(), httpRequest.getHeader("Host"));
            return;
        }
        //Todo 동적 html 생성.
        if (model != null) {
            HttpResponseUtils.dynamicResponse(httpResponse, controllerResults[0], model);
            return;
        }
        HttpResponseUtils.staticResponse(httpResponse, controllerResults[0]);
    }


    public void service(HttpRequest request, HttpResponse response) throws InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException, InstantiationException {
        try {
            Method method = methodFromUrl(request);
            if (method == null) {
                try {
                    HttpResponseUtils.staticResponse(response, request.url());
                } catch (IOException exception) {
                    HttpResponseUtils.notFoundResponse(response);
                }
                return;
            }
            Map data = dataFromRequest(request);
            Object controller = this.methodClassMap.get(method);
            List<Object> arguments = methodArguments(method, data, request, response);
            //Todo contorllerResult를 Serialize해서 json으로 던져줄 수도 있다.
            String controllerResult = (String) method.invoke(controller, arguments.toArray());
            Optional<Object> opt = arguments.stream().filter(o->Arrays.asList(o.getClass().getInterfaces()).contains(Model.class)).findAny();
            if(opt.isEmpty())
                controllerResponse(controllerResult, null, request, response);
            else
                controllerResponse(controllerResult, (Model) opt.get(), request, response);
        } catch (Exception e) {
            log.error(String.valueOf(e));
            e.printStackTrace();
            HttpResponseUtils.serverErrorResponse(response);
        }
    }

    public Set<Class> findAllClassesUsingReflectionsLibrary(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class)
                .stream()
                .collect(Collectors.toSet());
    }

}
