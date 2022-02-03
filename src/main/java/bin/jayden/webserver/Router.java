package bin.jayden.webserver;


import bin.jayden.http.*;
import bin.jayden.util.AnnotationProcessor;
import bin.jayden.util.Constants;
import bin.jayden.util.ParameterProcessor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

public class Router {
    private static final byte[] NOT_FOUNT_MESSAGE = "없는 페이지 입니다.".getBytes();
    private static final String REDIRECT = "redirect:";
    private static final Map<String, Method> postRoutingMap;
    private static final Map<String, Method> getRoutingMap;


    static {
        getRoutingMap = Collections.unmodifiableMap(AnnotationProcessor.getGetRoutingMap());
        postRoutingMap = Collections.unmodifiableMap(AnnotationProcessor.getPostRoutingMap());
    }


    public static MyHttpResponse routing(MyHttpRequest request) throws InvocationTargetException, IOException, IllegalAccessException {

        switch (request.getMethod()) {
            case Constants.HTTP_METHOD_POST:
                return postRouting(request);
            case Constants.HTTP_METHOD_GET:
                return getRouting(request);
        }


        return new MyHttpResponse.Builder().setBody(NOT_FOUNT_MESSAGE).setStatusCode(HttpStatusCode.STATUS_CODE_404).build();

    }

    private static MyHttpResponse getRouting(MyHttpRequest request) throws InvocationTargetException, IllegalAccessException, IOException {
        MyHttpResponse.Builder responseBuilder = new MyHttpResponse.Builder();
        Method responseMethod = getRoutingMap.get(request.getPath());
        if (responseMethod != null) {

            routing(responseMethod, request, responseBuilder);

        } else { //라우팅 맵에 URL에 해당하는 리소스를 찾아본다.

            File file = new File(Constants.RESOURCE_PATH + request.getPath());

            if (file.isFile()) {
                byte[] body = Files.readAllBytes(file.toPath());
                responseBuilder.setStatusCode(HttpStatusCode.STATUS_CODE_200).setMime(request.getMime()).setBody(body);
            } else {
                responseBuilder.setStatusCode(HttpStatusCode.STATUS_CODE_404).setBody(NOT_FOUNT_MESSAGE);
            }
        }
        return responseBuilder.build();
    }


    private static MyHttpResponse postRouting(MyHttpRequest request) throws InvocationTargetException, IllegalAccessException {
        MyHttpResponse.Builder responseBuilder = new MyHttpResponse.Builder();
        Method responseMethod = postRoutingMap.get(request.getPath());
        if (responseMethod != null) {
            routing(responseMethod, request, responseBuilder);
        } else {
            responseBuilder.setStatusCode(HttpStatusCode.STATUS_CODE_404).setBody(NOT_FOUNT_MESSAGE);
        }
        return responseBuilder.build();
    }

    private static void routing(Method responseMethod, MyHttpRequest request, MyHttpResponse.Builder responseBuilder) throws InvocationTargetException, IllegalAccessException {
        MyHttpSession session = MyHttpSessionFactory.getSession(request.getCookies().get(Constants.COOKIE_SESSION_KEY), responseBuilder);
        Parameter[] methodParameters = responseMethod.getParameters();
        Object[] parameters = ParameterProcessor.getParameterObjects(methodParameters, request, session);
        String body = (String) responseMethod.invoke(AnnotationProcessor.getInstanceByClass(responseMethod.getDeclaringClass()), parameters);
        if (body.startsWith(REDIRECT)) {
            body = body.replace(REDIRECT, "");
            responseBuilder.setStatusCode(HttpStatusCode.STATUS_CODE_302).addHeader("Location", body);
        } else {
            responseBuilder.setStatusCode(HttpStatusCode.STATUS_CODE_200).setBody(body);
        }
    }
}
