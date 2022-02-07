package controller;

import http.HttpMethod;
import model.ModelAndView;
import http.request.Request;
import util.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

public interface Controller {
//    ModelAndView proceed(Request request);

    default ModelAndView proceed(Request request) {
        Class<?> currentClass = getClass();

        try{
            Controller controller = (Controller) currentClass.getMethod("getInstance").invoke(null);
            Method method = aliasMethod(request.getMethod(), request.getUrl(), currentClass);
            return (ModelAndView) method.invoke(controller, request);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("실행 중 오류가 있습니다.");
        }
    }

    default Method aliasMethod(HttpMethod method, String url, Class<?> currentClass) throws NoSuchMethodException {
        return Arrays.stream(currentClass.getDeclaredMethods())
                .filter(m -> {
                    RequestMapping rm = m.getAnnotation(RequestMapping.class);
                    return method.equals(rm.method()) && url.equals(rm.url());
                })
                .findAny()
                .orElseThrow(() -> new NoSuchMethodException("존재하는 메서드가 없습니다."));
    }
}
