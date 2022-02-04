package framework;

import com.google.common.collect.Maps;
import framework.params.HttpRequest;
import framework.params.HttpResponse;
import framework.params.Params;
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

    public DataOutputStream request(InputStream in, OutputStream out) throws IOException {
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
        String[] tokens = br.readLine().split(" ");
        String[] urlTokens = tokens[1].split("\\?");
        String method = tokens[0];
        String url = urlTokens[0];

        HttpRequest reqeust = new HttpRequest();
        reqeust.setMethod(method);
        reqeust.setUrl(url);
        reqeust.setRequestParam(getRequestParam(urlTokens));
        reqeust.setRequestBody(getRequestBody(br, method));

        return reqeust;
    }

    private Map<String, String> getRequestParam(String[] urlTokens) {
        if (urlTokens.length < 2) {
            return Maps.newHashMap();
        }

        String queryString = URLDecoder.decode(urlTokens[1], StandardCharsets.UTF_8);
        return parseQueryString(queryString);
    }

    private Map<String, String> getRequestBody(BufferedReader br, String method) throws IOException {
        //method가 post, put, patch가 아닐 경우 빈 해쉬맵 리턴
        if (method.equals("GET") || method.equals("DELETE")) {
            return Maps.newHashMap();
        }

        String line;
        int contentLength = 0;
        while (!"".equals(line = br.readLine())) {
            if (line == null) return Maps.newHashMap();
            if (line.contains("Content-Length")) {
                contentLength = Integer.parseInt(line.split(": ")[1]);
            }
        }

        //request body가 줄바꿈으로 끝나지 않기 때문에 readline()을 쓸 수 없다!!!
        String encodedString = IOUtils.readData(br, contentLength);
        String queryString = URLDecoder.decode(encodedString, StandardCharsets.UTF_8);
        return parseQueryString(queryString);
    }

    private Map<String, String> parseQueryString(String queryString) {
        return HttpRequestUtils.parseQueryString(queryString);
    }
}
