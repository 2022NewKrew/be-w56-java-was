package controller.adapter;

import http.header.HttpHeaders;
import http.request.HttpRequest;
import http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import util.ResourceUtils;

@Slf4j
public class StaticHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(HttpRequest request) {
        return ResourceUtils.existResource(request.getUrl());
//        File file = new File(GlobalConfig.WEB_ROOT + request.getUrl());
//        URL resource = this.getClass().getClassLoader().getResource(request.getUrl());
//        if (resource == null)
//            return false;
//        File file = new File(resource.getPath());
//        return file.isFile();
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
//            byte[] body = Files.readAllBytes(new File(WEB_ROOT + url).toPath());
//            byte[] body = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(url).getPath()));
        byte[] body = ResourceUtils.getResource(url);
        response.addHeader(HttpHeaders.CONTENT_TYPE, getContentType(url, request.getHeader(HttpHeaders.ACCEPT)));
        response.body(body);
    }

    private static String getContentType(String url, String defaultValue) {
        String extension = url.substring(url.lastIndexOf(".") + 1);
        switch (extension) {
            case "html":
                return "text/html;charset=utf-8";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            default:
                return defaultValue;
        }
    }
}
