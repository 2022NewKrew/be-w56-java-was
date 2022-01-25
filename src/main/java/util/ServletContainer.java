package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.annotation.Controller;
import util.annotation.GetMapping;
import util.annotation.PostMapping;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
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
        Iterator<Class> classIterator = s.iterator();
        while(classIterator.hasNext()){
            Class controllerClass = Class.forName(classIterator.next().getName());
            if(controllerClass.getAnnotation(Controller.class) == null) continue;
            componentMapping(controllerClass, controllerClass.getMethods());
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
        if((postMapping = method.getAnnotation(PostMapping.class)) != null){
            postUrlMethodMap.put(postMapping.url(), method);
            return;
        }
    }

    public void service(HttpRequest request, HttpResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        Method method = null;
        Object controller = null;
        Map data = null;

        if(request.method() == HttpMethod.GET) {
            method = this.getUrlMethodMap.get(request.url());
            data = request.params();
        }
        if(request.method() == HttpMethod.POST){
            method = this.postUrlMethodMap.get(request.url());
            data = request.body();
        }

        controller = this.methodClassMap.get(method);

        if(method == null) {
            // 일단 static file은 그냥 서빙하도록 한다.
            String location = request.url();
            response.setStatus(HttpStatus.OK);
            byte[] body = Files.readAllBytes(new File("./webapp" + location).toPath());
            response.setHeader("Content-Type", "text/html;charset=utf-8");
            response.setHeader("Content-Length", String.valueOf(body.length));
            response.setBody(body);
        }

        List<Object> arguments = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for(Parameter parameter : parameters){
            if(parameter.getType() == Map.class)
                arguments.add(data);
            if(parameter.getType() == HttpRequest.class)
                arguments.add(request);
            if(parameter.getType() == HttpResponse.class)
                arguments.add(response);
        }

        String controllerResult = (String) method.invoke(controller, arguments.toArray());

        String[] controllerResults = controllerResult.split(":");
        if(controllerResults[0].equals("redirect")){
            response.setStatus(HttpStatus.FOUND);
            String location = controllerResults[1].trim();
            response.setHeader("Location", String.format("http://%s%s", request.getHeader("Host"), location));
        }else{
            String location = controllerResults[0];
            response.setStatus(HttpStatus.OK);
            byte[] body = Files.readAllBytes(new File("./webapp" + location).toPath());
            if(controller.getClass().getAnnotation(Controller.class) != null)
                response.setHeader("Content-Type", "text/html;charset=utf-8");
            response.setHeader("Content-Length", String.valueOf(body.length));
            response.setBody(body);
        }
    }


    public static class AccessingAllClassesInPackage {

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
