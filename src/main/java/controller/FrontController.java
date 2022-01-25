package controller;

import view.ViewResolver;
import webserver.HandlerMapping;
import webserver.RequestInfo;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Spring의 DispatcherServlet과 유사한 기능 수행
 */
public class FrontController {

    private static final HandlerMapping handlerMapping = new HandlerMapping();
    private static final ViewResolver viewResolver = new ViewResolver();

    public DataOutputStream request(InputStream in, OutputStream out) throws IOException {
        try {
            String viewName = requestToController(in);
            return viewResolver.getResponse(out, viewName);
        } catch (Exception e) {
            e.printStackTrace();
            return viewResolver.getResponse(out, "error");
        }
    }

    private String requestToController(InputStream in) throws InvocationTargetException, IllegalAccessException, IOException {
        RequestInfo requestInfo = parseRequest(in);
        return handlerMapping.requestToController(requestInfo);
    }

    private RequestInfo parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] tokens = br.readLine().split(" ");
        RequestInfo requestInfo = new RequestInfo();

        requestInfo.setMethod(tokens[0]);
        requestInfo.setUrl(tokens[1]);

        // url(쿼리 파라미터 제외)에 '.'이 포함된 경우 static resource를 요청하는 것으로 판단
        if (requestInfo.getUrl().contains(".")) {
            requestInfo.setStaticResource(true);
        }

        return requestInfo;
    }
}
