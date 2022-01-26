package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.annotation.Controller;
import util.annotation.GetMapping;
import util.annotation.PostMapping;
import util.http.HttpMethod;
import util.http.HttpRequest;
import util.http.HttpResponse;
import util.http.HttpResponseUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class ServletContainer {
    private static final Logger log = LoggerFactory.getLogger(ServletContainer.class);
    private final Map<String, Method> getUrlMethodMap;
    private final Map<String, Method> postUrlMethodMap;
    private final Map<Method, Object> methodClassMap;

    public ServletContainer() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        getUrlMethodMap = new HashMap<>();
        postUrlMethodMap = new HashMap<>();
        methodClassMap = new HashMap<>();

        AccessingAllClassesInPackage aac = new AccessingAllClassesInPackage();
        Set<Class> s = aac.findAllClassesUsingClassLoader("controller");
        for (Class controller : s.toArray(new Class[0])) {
            if (controller.getAnnotation(Controller.class) == null) continue;
            componentMapping(controller, controller.getMethods());
        }
    }

    private void componentMapping(Class controllerClass, Method[] methods) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for(Method method : methods) {
            methodControllerMapping(method, controllerClass);
            urlMethodMapping(method);
        }
    }

    private void methodControllerMapping(Method method, Class controllerClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        methodClassMap.put(method, controllerClass.getConstructor().newInstance());
    }

    private void urlMethodMapping(Method method){
        GetMapping getMapping = null;
        PostMapping postMapping = null;

        if ((getMapping = method.getAnnotation(GetMapping.class)) != null) {
            getUrlMethodMap.put(getMapping.url(), method);
            return;
        }
        if ((postMapping = method.getAnnotation(PostMapping.class)) != null) {
            postUrlMethodMap.put(postMapping.url(), method);
            return;
        }
    }

    private Method methodFromUrl(HttpRequest httpRequest) {
        if (httpRequest.method() == HttpMethod.GET)
            return this.getUrlMethodMap.get(httpRequest.url());
        if (httpRequest.method() == HttpMethod.POST)
            return this.postUrlMethodMap.get(httpRequest.url());
        return null;

    }

    private Map<String, String> dataFromRequest(HttpRequest httpRequest) {
        if (httpRequest.method() == HttpMethod.GET)
            return httpRequest.params();
        if (httpRequest.method() == HttpMethod.POST)
            return httpRequest.body();
        return null;
    }

    private List<Object> methodArguments(Method method, Map data, HttpRequest httpRequest, HttpResponse httpResponse) {
        List<Object> arguments = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            arguments.add(methodArgument(parameter, data, httpRequest, httpResponse));
        }
        return arguments;
    }

    private Object methodArgument(Parameter parameter, Map data, HttpRequest httpRequest, HttpResponse httpResponse) {
        if (parameter.getType() == Map.class)
            return data;
        if (parameter.getType() == HttpRequest.class)
            return httpRequest;
        if (parameter.getType() == HttpResponse.class)
            return httpResponse;
        return null;
    }

    private void controllerResponse(String controllerResult, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String[] controllerResults = controllerResult.split(":");
        if (controllerResults[0].equals("redirect")) {
            HttpResponseUtils.redirectResponse(httpResponse, controllerResults[1].trim(), httpRequest.getHeader("Host"));
            return;
        }
        HttpResponseUtils.staticResponse(httpResponse, controllerResults[0]);
    }


    public void service(HttpRequest request, HttpResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        Method method = methodFromUrl(request);
        if (method == null) {
            HttpResponseUtils.staticResponse(response, request.url());
            return;
        }
        Map data = dataFromRequest(request);
        Object controller = this.methodClassMap.get(method);
        List<Object> arguments = methodArguments(method, data, request, response);
        String controllerResult = (String) method.invoke(controller, arguments.toArray());
        controllerResponse(controllerResult, request, response);
    }


    private static class AccessingAllClassesInPackage {

        public Set<Class> findAllClassesUsingClassLoader(String packageName) {
            InputStream stream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(packageName.replaceAll("[.]", "/"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(line -> getClass(line, packageName))
                    .collect(Collectors.toSet());
        }

        private Class getClass(String className, String packageName) {
            try {
                return Class.forName(packageName + "."
                        + className.substring(0, className.lastIndexOf('.')));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
