package framework;

import com.google.common.collect.Maps;
import framework.params.*;
import framework.util.HttpRequestUtils;
import framework.util.IOUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Spring의 DispatcherServlet과 유사한 기능 수행
 */
public class FrontController {

    private final HandlerMapping handlerMapping;
    private final ViewResolver viewResolver;

    public FrontController(HandlerMapping handlerMapping, ViewResolver viewResolver) {
        this.handlerMapping = handlerMapping;
        this.viewResolver = viewResolver;
    }

    public DataOutputStream request(InputStream in, OutputStream out) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Params params = new Params();
        try {
            String viewName = requestToController(in, params);
            HttpResponse response = viewResolver.getResponse(viewName, params);
            return response.getResponse(out);
        } catch (Exception e) {
            e.printStackTrace();
            HttpResponse response = viewResolver.getResponse("error", params);
            return response.getResponse(out);
        }
    }

    private String requestToController(InputStream in, Params params) throws InvocationTargetException, IllegalAccessException, IOException {
        HttpRequest request = parseRequest(in);
        return handlerMapping.requestToController(request, params);
    }

    private HttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        RequestHeaderParser headerParser = new RequestHeaderParser();
        headerParser.parseHeader(br);

        HttpRequest reqeust = new HttpRequest();
        reqeust.setMethod(headerParser.getMethod());
        reqeust.setUrl(headerParser.getUrl());
        reqeust.setRequestParam(headerParser.getRequestParam());
        reqeust.setCookie(headerParser.getCookie());
        reqeust.setRequestBody(getRequestBody(br, headerParser.getContentLength(), headerParser.getMethod()));

        return reqeust;
    }

    private Map<String, String> getRequestBody(BufferedReader br, int contentLength, String method) throws IOException {
        //method가 post, put, patch가 아닐 경우 빈 해쉬맵 리턴
        if (method.equals("GET") || method.equals("DELETE")) {
            return Maps.newHashMap();
        }
        //request body가 줄바꿈으로 끝나지 않기 때문에 readline()을 쓸 수 없다!!!
        String encodedString = IOUtils.readData(br, contentLength);
        String queryString = URLDecoder.decode(encodedString, StandardCharsets.UTF_8);
        return HttpRequestUtils.parseQueryString(queryString);
    }
}
