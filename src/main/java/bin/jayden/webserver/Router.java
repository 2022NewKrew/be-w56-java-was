package bin.jayden.webserver;


import bin.jayden.http.HttpStatusCode;
import bin.jayden.http.MyHttpRequest;
import bin.jayden.http.MyHttpResponse;
import bin.jayden.http.MyHttpSession;
import bin.jayden.util.AnnotationProcessor;
import bin.jayden.util.Constants;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private static final byte[] NOT_FOUNT_MESSAGE = "없는 페이지 입니다.".getBytes();
    private static final String COOKIE_SESSION_KEY = "SESSIONID";
    private static final String REDIRECT = "redirect:";
    private static final Map<String, Method> postRoutingMap;
    private static final Map<String, Method> getRoutingMap;
    private static final Map<String, MyHttpSession> sessionMap = new HashMap<>();
    private static final SecureRandom secureRandom = new SecureRandom();


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

            File file = new File("./webapp" + request.getPath());

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
        MyHttpSession session = getSession(request.getCookies().get(COOKIE_SESSION_KEY), responseBuilder);
        String body = (String) responseMethod.invoke(AnnotationProcessor.getInstanceByClass(responseMethod.getDeclaringClass()), session, request);
        if (body.startsWith(REDIRECT)) {
            body = body.replace(REDIRECT, "");
            responseBuilder.setStatusCode(HttpStatusCode.STATUS_CODE_302).addHeader("Location", body);
        } else {
            responseBuilder.setStatusCode(HttpStatusCode.STATUS_CODE_200).setBody(body);
        }
    }

    private static MyHttpSession getSession(String sessionId, MyHttpResponse.Builder responseBuilder) {
        MyHttpSession session = sessionMap.get(sessionId);
        if (session == null) {
            session = new MyHttpSession();

            byte[] secureBytes = new byte[16];
            secureRandom.nextBytes(secureBytes);
            String newSessionId = bytesToHex(secureBytes);
            sessionMap.put(newSessionId, session);
            responseBuilder.addHeader("Set-Cookie", COOKIE_SESSION_KEY + "=" + newSessionId + "; Path=/");
        }
        return session;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
